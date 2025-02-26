package scraping.gookmin;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CookieUtil {
    public static String getCookie(ResponseEntity<?> response){
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (cookies != null && !cookies.isEmpty()) {
            return String.join("; ", cookies);
        }
        else throw new IllegalArgumentException("쿠키를 가져올 수 없습니다.");
    }
}
