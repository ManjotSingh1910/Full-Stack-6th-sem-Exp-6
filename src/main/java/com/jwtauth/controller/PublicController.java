package com.jwtauth.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * PublicController — publicly accessible routes (no authentication required).
 */
@RestController
public class PublicController {

    /**
     * GET /
     * Root landing page — shows API overview.
     */
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> root() {
        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <title>JWT Auth API</title>
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', sans-serif; background: #0f172a; color: #e2e8f0; min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 2rem; }
                    .card { background: #1e293b; border-radius: 16px; padding: 2.5rem; max-width: 700px; width: 100%; box-shadow: 0 25px 50px rgba(0,0,0,0.5); }
                    .badge { display: inline-block; background: #22c55e; color: #fff; font-size: 0.75rem; font-weight: 700; padding: 0.25rem 0.75rem; border-radius: 999px; margin-bottom: 1rem; letter-spacing: 1px; }
                    h1 { font-size: 2rem; font-weight: 800; margin-bottom: 0.5rem; background: linear-gradient(90deg, #60a5fa, #a78bfa); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
                    p.sub { color: #94a3b8; margin-bottom: 2rem; }
                    h2 { font-size: 1rem; font-weight: 600; color: #64748b; text-transform: uppercase; letter-spacing: 1px; margin: 1.5rem 0 0.75rem; }
                    .endpoint { display: flex; align-items: center; gap: 0.75rem; padding: 0.6rem 0.9rem; border-radius: 8px; background: #0f172a; margin-bottom: 0.5rem; font-size: 0.9rem; }
                    .method { font-weight: 700; font-size: 0.75rem; padding: 0.2rem 0.55rem; border-radius: 5px; min-width: 50px; text-align: center; }
                    .get  { background: #1d4ed8; color: #bfdbfe; }
                    .post { background: #15803d; color: #bbf7d0; }
                    .path { color: #e2e8f0; font-family: monospace; }
                    .desc { color: #64748b; font-size: 0.8rem; margin-left: auto; }
                    .footer { margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #334155; color: #475569; font-size: 0.8rem; text-align: center; }
                    a { color: #60a5fa; text-decoration: none; }
                </style>
            </head>
            <body>
                <div class="card">
                    <div class="badge">● LIVE</div>
                    <h1>JWT Auth API</h1>
                    <p class="sub">Spring Boot 3 · Spring Security 6 · JJWT · H2 Database</p>

                    <h2>Public Endpoints</h2>
                    <div class="endpoint"><span class="method post">POST</span><span class="path">/api/auth/register</span><span class="desc">Register new user</span></div>
                    <div class="endpoint"><span class="method post">POST</span><span class="path">/api/auth/login</span><span class="desc">Login &amp; get JWT token</span></div>
                    <div class="endpoint"><span class="method post">POST</span><span class="path">/api/auth/logout</span><span class="desc">Logout</span></div>
                    <div class="endpoint"><span class="method get">GET</span><span class="path">/api/public/health</span><span class="desc">Health check</span></div>

                    <h2>Protected Endpoints (JWT Required)</h2>
                    <div class="endpoint"><span class="method get">GET</span><span class="path">/api/protected/dashboard</span><span class="desc">User dashboard</span></div>
                    <div class="endpoint"><span class="method get">GET</span><span class="path">/api/protected/profile</span><span class="desc">User profile</span></div>
                    <div class="endpoint"><span class="method get">GET</span><span class="path">/api/protected/admin</span><span class="desc">Admin only</span></div>

                    <div class="footer">
                        Full Stack Lab · Exp 6 &nbsp;|&nbsp; <a href="/api/public/health">Health Check ↗</a>
                    </div>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }

    @GetMapping("/api/public/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "JWT Auth Demo",
                "message", "This is a public endpoint. No authentication required."
        ));
    }
}
