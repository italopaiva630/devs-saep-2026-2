package com.login.atividade.sessoes;

import jakarta.servlet.http.HttpSession;

public class SessaoUtil {

    private static final String SESSAO_USUARIO = "usuarioLogado";

    public static void RegistrarSessao(HttpSession session, SessaoDto sessaoDto) {
        session.setAttribute(SESSAO_USUARIO, sessaoDto);
    }

    public static SessaoDto ObterSessao(HttpSession session) {
        return (SessaoDto) session.getAttribute(SESSAO_USUARIO);
    }

    public static void RemoverSessao(HttpSession session) {
        session.removeAttribute(SESSAO_USUARIO);
    }
}
