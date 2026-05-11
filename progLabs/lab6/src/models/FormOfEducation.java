package src.models;

/**
 * Перечисление возможных форм обучения для учебных групп.
 * Используется для задания типа образовательного процесса в объекте StudyGroup.
 */
public enum FormOfEducation {
    /**
     * Дистанционное обучение.
     */
    DISTANCE_EDUCATION,

    /**
     * Очное (дневное) обучение.
     */
    FULL_TIME_EDUCATION,

    /**
     * Вечерние курсы (очно-заочная форма).
     */
    EVENING_CLASSES
}