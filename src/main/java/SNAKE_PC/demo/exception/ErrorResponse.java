package SNAKE_PC.demo.exception;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private LocalDateTime timestamp;
    
    private int status;
    
    private String error;
    
    private String mensaje;
    
    private String path;
    
    private List<FieldError> erroresValidacion;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        private String campo;
        private String mensaje;
        private Object valorRechazado;
    }
}
