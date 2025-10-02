import express from 'express';
import fetch from 'node-fetch';
import { createRemoteJWKSet, jwtVerify } from '@panva/jose';

const app = express();

const PORT = process.env.PORT || 3000;
const TARGET_BASE_URL = process.env.TARGET_BASE_URL || 'http://localhost:8080';
const OIDC_ISSUER = process.env.OIDC_ISSUER || 'http://localhost:8081/realms/mandel';
const OIDC_AUDIENCE = process.env.OIDC_AUDIENCE || 'mandel-gateway';
const OIDC_JWKS_URI = process.env.OIDC_JWKS_URI || `${OIDC_ISSUER}/protocol/openid-connect/certs`;
const REQUIRE_EMAIL_VERIFIED = String(process.env.REQUIRE_EMAIL_VERIFIED || 'false').toLowerCase() === 'true';

const BLACKLISTED_EMAILS = new Set(
  (process.env.BLACKLISTED_EMAILS || '')
    .split(',')
    .map((e) => e.trim().toLowerCase())
    .filter(Boolean)
);

const jwks = createRemoteJWKSet(new URL(OIDC_JWKS_URI));

async function verifyToken(authorizationHeader) {
  if (!authorizationHeader?.startsWith('Bearer ')) {
    throw Object.assign(new Error('missing_bearer'), { status: 401 });
  }
  const token = authorizationHeader.slice('Bearer '.length);

  const { payload } = await jwtVerify(token, jwks, {
    issuer: OIDC_ISSUER,
    audience: OIDC_AUDIENCE,
  });

  const email = String(payload.email || '').toLowerCase();
  const emailVerified = Boolean(payload.email_verified);

  if (!email) {
    throw Object.assign(new Error('email_missing'), { status: 403 });
  }
  if (BLACKLISTED_EMAILS.has(email)) {
    throw Object.assign(new Error('email_blacklisted'), { status: 403 });
  }
  if (REQUIRE_EMAIL_VERIFIED && !emailVerified) {
    throw Object.assign(new Error('email_not_verified'), { status: 403 });
  }

  return { email, sub: payload.sub };
}

app.use(async (req, res) => {
  try {
    await verifyToken(req.headers.authorization);

    const targetUrl = new URL(req.url, TARGET_BASE_URL);
    const upstreamResp = await fetch(targetUrl, {
      method: req.method,
      headers: {
        ...Object.fromEntries(Object.entries(req.headers).filter(([k]) => k.toLowerCase() !== 'host')),
      },
      body: ['GET', 'HEAD'].includes(req.method.toUpperCase()) ? undefined : req,
    });

    res.status(upstreamResp.status);
    // Clone headers
    upstreamResp.headers.forEach((value, key) => {
      res.setHeader(key, value);
    });
    upstreamResp.body.pipe(res);
  } catch (err) {
    const status = err.status || 500;
    res.status(status).json({ error: err.message || 'internal_error' });
  }
});

app.listen(PORT, () => {
  console.log(`Auth gateway listening on http://localhost:${PORT}`);
});

