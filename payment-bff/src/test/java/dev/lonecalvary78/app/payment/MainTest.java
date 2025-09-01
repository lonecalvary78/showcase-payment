package dev.lonecalvary78.app.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PaymentBFFApplication Tests")
class PaymentBFFApplicationTest {

    @Test
    @DisplayName("Should validate main class exists")
    void shouldValidateMainClassExists() {
        // Given
        var clazz = PaymentBFFApplication.class;

        // When & Then
        assertThat(clazz).isNotNull();
        assertThat(clazz.getDeclaredMethods()).hasSizeGreaterThan(0);
        
        // Check for main method
        Method[] methods = clazz.getDeclaredMethods();
        var hasMainMethod = java.util.Arrays.stream(methods)
            .anyMatch(method -> "main".equals(method.getName()) 
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == String[].class);
                
        assertThat(hasMainMethod).isTrue();
    }

    @Test
    @DisplayName("Should validate main method signature")
    void shouldValidateMainMethodSignature() throws NoSuchMethodException {
        // Given
        var clazz = PaymentBFFApplication.class;

        // When
        var mainMethod = clazz.getMethod("main", String[].class);

        // Then
        assertThat(mainMethod).isNotNull();
        assertThat(mainMethod.getReturnType()).isEqualTo(void.class);
        assertThat(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers())).isTrue();
        assertThat(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("Should validate class structure")
    void shouldValidateClassStructure() {
        // Given
        var clazz = PaymentBFFApplication.class;

        // When & Then
        assertThat(clazz.getPackage().getName()).isEqualTo("dev.lonecalvary78.app.payment");
        assertThat(java.lang.reflect.Modifier.isPublic(clazz.getModifiers())).isTrue();
    }
}
