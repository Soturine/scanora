# Publicação

## GitHub Pages

O site estático está em `docs/` e o workflow `pages.yml` publica automaticamente a branch principal no GitHub Pages.

## GitHub Releases

Recomendações:

- use tags seguindo SemVer;
- gere notas de release com base no `CHANGELOG.md`;
- publique APK/AAB apenas quando o pipeline de release estiver estabilizado.

## Play Store

Checklist resumido:

1. Revisar nome final, ícone e screenshots.
2. Validar a política de privacidade publicada.
3. Revisar permissões solicitadas.
4. Confirmar `targetSdk` e políticas do Google Play.
5. Gerar `AAB` de release.
6. Testar fluxo em dispositivos reais.

## Assinatura e release

Itens ainda não automatizados no MVP:

- assinatura de release;
- pipeline de Play Console;
- screenshots reais e criativos finais;
- hardening completo de minificação/obfuscation.

