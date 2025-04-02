package ut.edu.childgrowth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "2ULGBsaD4Zb3w1XFlC93Yf2YtfolWETV6UmGhChgIfw"; // Nên lưu trong application.properties để bảo mật

    // Tạo token với thông tin username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Lưu username vào token
                .setIssuedAt(new Date()) // Thời gian phát hành token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Hết hạn sau 1 giờ
                .signWith(SignatureAlgorithm.HS256, secretKey) // Ký token bằng secret key
                .compact();
    }

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Xác thực token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Kiểm tra token có hết hạn không
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}