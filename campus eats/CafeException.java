public class CafeException extends Exception {

    public CafeException(String message) {
        super(message);
    }
}

class InvalidQuantityException extends CafeException {

    public InvalidQuantityException(String message) {
        super(message);
    }
}

class ItemNotFoundException extends CafeException {

    public ItemNotFoundException(String message) {
        super(message);
    }
}

class EmptyOrderException extends CafeException {

    public EmptyOrderException(String message) {
        super(message);
    }
}

class OrderNotFoundException extends CafeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}