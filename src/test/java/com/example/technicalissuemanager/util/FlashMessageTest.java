package com.example.technicalissuemanager.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FlashMessageTest {

    @Test
    void storesSuccessMessageInSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        FlashMessage.setSuccess(request, "保存しました。");

        verify(session).setAttribute("successMessage", "保存しました。");
    }

    @Test
    void consumesAndRemovesSuccessMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("successMessage")).thenReturn("保存しました。");

        String message = FlashMessage.consumeSuccess(request);

        assertEquals("保存しました。", message);
        verify(session).removeAttribute("successMessage");
    }

    @Test
    void returnsNullWhenSessionDoesNotExist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        assertNull(FlashMessage.consumeSuccess(request));
    }

    @Test
    void ignoresNonTextValueAndStillRemovesIt() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("successMessage")).thenReturn(123);

        assertNull(FlashMessage.consumeSuccess(request));
        verify(session).removeAttribute("successMessage");
    }
}
