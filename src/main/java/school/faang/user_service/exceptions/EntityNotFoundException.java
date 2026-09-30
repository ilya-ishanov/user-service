package school.faang.user_service.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, Long id) {
        super(" " + entityName + " с id=" + id + " не найден");
    }
}
