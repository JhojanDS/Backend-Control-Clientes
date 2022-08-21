package jhojands.controlclientes.auth.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    private final Log logger = LogFactory.getLog(this.getClass());

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handlerDataIntegrityViolationException(DataIntegrityViolationException e) throws NoSuchFieldException {


        String cause = e.getRootCause().getMessage();
        String msg = convertMsg(cause);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", msg);
        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));


        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);


    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handlerDataAcessException(DataAccessException e) {

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Error en la base de datos");
        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private String convertMsg(String cause) {
        logger.info("entrando a convertMsg");
        String field = converField(cause);
        String data = convertData(cause);

        return "El campo '" + field + "' ya esta registrado con otro usuario. Dato duplicado: " + data;
    }

    private String converField(String cause) {

        if (cause.contains("'users.UK_88ud7ehaqhr1hexbhnpmph9wy'")) {
            return "identification";

        } else {// (cause.contains("'users.UK_6dotkott2kjsp8vw4d0m25fb7'"))
            return "email";
        }

    }

    private String convertData(String cause) {

        int a = 0, b = 0;
        boolean startFound = false;

        for (int i = 0; i < cause.length(); i++) {

            if (!startFound) {

                if (cause.charAt(i) == '\'') {
                    a = i;
                    startFound = true;
                    i++;

                }
            }
            if (startFound) {
                if (cause.charAt(i) == '\'') {
                    b = i;
                    break;
                }
            }
        }
        return cause.substring(a, ++b);
    }


}
