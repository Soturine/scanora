# Segurança

## Como reportar

Se você encontrar uma vulnerabilidade:

1. Não abra issue pública.
2. Use o fluxo privado de security advisory do GitHub, quando disponível.
3. Caso isso ainda não esteja configurado, entre em contato com os maintainers pelo canal privado disponível no GitHub do repositório.

## O que é considerado vulnerabilidade

- Leitura indevida de arquivos exportados ou temporários.
- Exposição acidental de documentos, OCR ou metadados.
- Bypass de permissões sensíveis.
- Compartilhamento de arquivos sem controle adequado de `Uri`/`FileProvider`.
- Código que permita execução indevida, escalonamento de privilégios ou exfiltração de dados.

## Política de resposta

- O projeto tentará confirmar o problema, classificar severidade e responder com um plano de correção.
- Correções críticas devem ser tratadas com prioridade antes de divulgação pública.
- Após a correção, a documentação pode ser atualizada com notas de segurança e impacto.

