# Contribuindo com o Scanora

## Pré-requisitos

- Git
- Android Studio com suporte a Kotlin e Compose
- JDK 17
- Android SDK Platform 36

## Fluxo sugerido

1. Faça um fork ou clone do repositório.
2. Crie uma branch descritiva a partir de `main`.
3. Implemente a mudança com escopo claro.
4. Rode lint e testes antes de abrir o PR.
5. Descreva motivação, comportamento esperado e riscos no pull request.

## Padrão de branch

Sugestões:

- `feat/nome-curto`
- `fix/nome-curto`
- `docs/nome-curto`
- `refactor/nome-curto`
- `test/nome-curto`

## Padrão de commit

Preferência do projeto:

- `feat: adiciona exportacao png`
- `fix: corrige ordenacao de paginas`
- `docs: atualiza setup do android studio`
- `test: cobre validacao de nome`

## Como abrir issues

- Use os templates em `.github/ISSUE_TEMPLATE`.
- Informe contexto, reprodução, comportamento atual e comportamento esperado.
- Se envolver segurança, não abra issue pública. Veja [SECURITY.md](SECURITY.md).

## Checklist de PR

- O escopo está claro e pequeno o suficiente para revisão?
- A mudança tem impacto documentado em comportamento?
- Há testes ou justificativa explícita para a ausência deles?
- Strings novas foram preparadas para futura internacionalização?
- Não houve introdução de dependência ou serviço externo desnecessário?

