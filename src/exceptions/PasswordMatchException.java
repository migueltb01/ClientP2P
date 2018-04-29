package exceptions;

public class PasswordMatchException extends Exception {

    public PasswordMatchException() {
        super("las contraseñas no coinciden");
    }
}
