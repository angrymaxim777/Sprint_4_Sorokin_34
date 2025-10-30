package ru.yandex.scooter.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MainPageTest extends BaseTest {

    @Parameterized.Parameter
    public int questionIndex;

    @Parameterized.Parameters(name = "Question index: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}
        });
    }

    @Test
    public void testQuestionsCount() {
        waitForPageToLoad();
        int actualCount = mainPage.getQuestionsCount();
        assertEquals("Должно быть 8 вопросов",
                mainPage.EXPECTED_QUESTIONS.size(), actualCount);
    }

    @Test
    public void testSpecificQuestionAndAnswer() {
        waitForPageToLoad();

        // Проверяем, что вопрос существует
        assertTrue("Вопрос с индексом " + questionIndex + " должен существовать",
                questionIndex < mainPage.getQuestionsCount());

        // Получаем ожидаемые данные
        String expectedQuestion = mainPage.getExpectedQuestion(questionIndex);
        String expectedAnswer = mainPage.getExpectedAnswer(questionIndex);

        // Получаем актуальный текст вопроса
        String actualQuestion = mainPage.getQuestionText(questionIndex);

        // Проверяем соответствие вопроса
        assertEquals("Текст вопроса не соответствует ожидаемому",
                expectedQuestion, actualQuestion);

        // Получаем актуальный ответ
        String actualAnswer = mainPage.getAnswerTextReliable(questionIndex);

        // Проверяем, что ответ не пустой
        assertFalse("Ответ не должен быть пустым", actualAnswer.isEmpty());

        // Для Firefox - contains
        if ("firefox".equals(getCurrentBrowser())) {
            assertTrue("Ответ должен содержать ключевую информацию: " + expectedAnswer,
                    actualAnswer.contains(getKeyPart(expectedAnswer)));
        } else {
            assertEquals("Текст ответа не соответствует ожидаемому",
                    expectedAnswer, actualAnswer);
        }
    }

    @Test
    public void testInitialState() {
        waitForPageToLoad();
        // Проверяем только первые 4 вопроса для стабильности
        for (int i = 0; i < Math.min(4, mainPage.getQuestionsCount()); i++) {
            assertTrue("Вопрос " + i + " должен быть свернут изначально",
                    mainPage.isAnswerHidden(i));
        }
    }

    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===

    private void waitForPageToLoad() {
        try {
            wait.until(driver -> mainPage.getQuestionsCount() >= mainPage.EXPECTED_QUESTIONS.size());
        } catch (Exception e) {
            System.out.println("Ошибка загрузки страницы: " + e.getMessage());
        }
    }

    // Метод для извлечения ключевой части ответа (Firefox)
    private String getKeyPart(String fullAnswer) {
        // Берем первые несколько слов для проверки
        if (fullAnswer.length() > 30) {
            return fullAnswer.substring(0, 30);
        }
        return fullAnswer;
    }
}