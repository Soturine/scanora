Você é um engenheiro de software sênior, arquiteto Android, product engineer, technical writer e maintainer open-source.

Sua missão é criar um projeto completo, profissional e publicável no GitHub para um app Android ORIGINAL de escaneamento de documentos, inspirado apenas na categoria de produto “scanner de documentos”, mas SEM copiar marca, identidade visual, textos, layout, assets, nomes de features, screenshots ou qualquer elemento proprietário de apps existentes.

================================================================
1. OBJETIVO DO PRODUTO
================================================================

Crie um app Android chamado provisoriamente:

NOME_DO_APP: Scanora
PACKAGE_NAME: com.seunome.scanora

O app deve permitir que o usuário:
- capture documentos pela câmera
- detecte e recorte automaticamente o papel
- corrija perspectiva
- aplique filtros de documento
- gere PDF com múltiplas páginas
- exporte imagens JPG/PNG
- compartilhe arquivos
- organize scans localmente
- rode bem como um MVP sólido e escalável

O projeto deve ser original, limpo, bem estruturado e pronto para evoluir.

================================================================
2. STACK E PADRÕES TÉCNICOS
================================================================

Use obrigatoriamente:

- Kotlin
- Android Studio / Gradle Kotlin DSL
- Jetpack Compose para UI
- Material 3
- CameraX para câmera
- ML Kit Document Scanner quando fizer sentido
- ML Kit Text Recognition para OCR
- Room para persistência local
- DataStore para preferências
- Navigation Compose
- ViewModel
- Coroutines + Flow
- WorkManager para tarefas em background leves
- geração de PDF local
- arquitetura modular e clara
- testes unitários básicos
- testes instrumentados mínimos quando aplicável

Desejo:
- código legível e profissional
- nomes claros
- comentários apenas onde realmente agregarem
- zero gambiarra
- tratar erros de permissão, câmera e exportação
- foco em Android moderno
- minSdk razoável
- targetSdk atual
- suporte inicial apenas a portrait
- interface em português do Brasil
- strings preparadas para futura internacionalização

================================================================
3. DIFERENCIAL DO PRODUTO
================================================================

O app não deve ser só um clone genérico.
Inclua diferenciais originais como:

- modo “Caderno / Faculdade”
- modo “Documento / Contrato”
- modo “Recibo / Nota”
- tags e organização local
- busca por título
- OCR local com cópia rápida do texto
- criação de lotes de páginas
- visual limpo e moderno
- modo escuro
- onboarding simples
- experiência rápida de escanear -> revisar -> exportar

Priorize privacidade:
- processamento local sempre que possível
- sem upload obrigatório
- explicar isso no README e na Privacy Policy

================================================================
4. ESCOPO FUNCIONAL
================================================================

Implemente o MVP com as seguintes telas:

1. Splash
2. Onboarding curto
3. Home
4. Captura por câmera
5. Importação de imagem da galeria
6. Tela de ajuste/crop
7. Tela de filtros
8. Revisão de páginas
9. Exportação
10. Histórico / Meus scans
11. Detalhe de um scan
12. Configurações
13. Tela de OCR / copiar texto
14. Sobre o app

Fluxos mínimos:
- abrir câmera
- capturar documento
- detectar bordas
- ajustar corte manualmente quando necessário
- aplicar filtros
- reordenar páginas
- excluir página
- renomear documento
- exportar PDF
- exportar imagem
- compartilhar
- salvar histórico local
- pesquisar scans
- abrir OCR em uma página
- copiar texto reconhecido

================================================================
5. REQUISITOS NÃO FUNCIONAIS
================================================================

O projeto deve:
- compilar sem erro
- ter estrutura escalável
- ser fácil de entender por outro desenvolvedor
- ter README realmente útil
- ter CHANGELOG inicial
- ter LICENSE
- ter CONTRIBUTING
- ter SECURITY
- ter CODE_OF_CONDUCT
- ter templates de issue e PR
- ter documentação técnica em /docs
- ter uma página estática para GitHub Pages
- ter GitHub Actions para build e checagens
- ter versão inicial 0.1.0 ou 1.0.0-beta
- usar conventional commits como recomendação de workflow
- usar semantic versioning no projeto

================================================================
6. ESTRUTURA DE REPOSITÓRIO DESEJADA
================================================================

Monte uma estrutura parecida com isto, adaptando quando necessário:

/app
/core
/core-ui
/core-common
/feature-home
/feature-camera
/feature-editor
/feature-export
/feature-history
/feature-settings
/feature-ocr
/docs
/.github
  /ISSUE_TEMPLATE
  /workflows
README.md
CHANGELOG.md
CONTRIBUTING.md
SECURITY.md
CODE_OF_CONDUCT.md
LICENSE
PRIVACY_POLICY.md
ROADMAP.md

Se modularizar demais atrapalhar o MVP, simplifique sem perder organização.

================================================================
7. DOCUMENTAÇÃO QUE VOCÊ DEVE CRIAR
================================================================

Crie documentação profissional, em português, incluindo:

README.md
- visão geral do app
- proposta de valor
- stack
- screenshots placeholders
- arquitetura resumida
- como rodar
- como contribuir
- roadmap
- status do projeto
- badges úteis
- observações de privacidade
- limitações atuais
- próximos passos

CHANGELOG.md
- formato organizado por versão
- incluir versão inicial
- categorias Added / Changed / Fixed / Planned

CONTRIBUTING.md
- como clonar
- como abrir issue
- padrão de branch
- padrão de commit
- checklist de PR

SECURITY.md
- política de reporte responsável
- o que é considerado vulnerabilidade
- como reportar

CODE_OF_CONDUCT.md
- padrão colaborativo simples e profissional

PRIVACY_POLICY.md
- política de privacidade coerente com o app
- explicar que documentos são processados localmente por padrão
- explicar permissões usadas
- explicar exportação e compartilhamento
- deixar claro que integrações futuras podem alterar a política

ROADMAP.md
- curto prazo
- médio prazo
- longo prazo

================================================================
8. GITHUB PAGES / SITE DO PROJETO
================================================================

Crie uma página estática pronta para GitHub Pages dentro de /docs ou outra estrutura simples compatível.

Esse site deve ter:
- index.html
- style.css
- opcionalmente script.js
- layout responsivo
- visual moderno e limpo
- hero section
- descrição do app
- lista de features
- screenshots placeholders
- bloco de privacidade
- bloco de roadmap
- bloco de instalação
- links para GitHub, releases e documentação
- seção de FAQ
- seção de status do projeto

Também crie o workflow necessário para publicar essa página no GitHub Pages automaticamente.

================================================================
9. GITHUB / AUTOMAÇÃO / DX
================================================================

Crie arquivos úteis em .github:

- pull_request_template.md
- ISSUE_TEMPLATE/bug_report.yml
- ISSUE_TEMPLATE/feature_request.yml
- ISSUE_TEMPLATE/config.yml
- workflows/android-ci.yml
- workflows/pages.yml

O CI deve:
- buildar o projeto
- rodar lint
- rodar testes unitários
- falhar se algo quebrar

================================================================
10. ARQUITETURA E QUALIDADE
================================================================

Quero que você tome decisões maduras de arquitetura.

Entregue:
- separação clara entre UI, domínio e dados
- models bem definidos
- state holders consistentes
- tratamento de eventos de UI
- navegação organizada
- temas claros/escuros
- componentes reutilizáveis
- preview de componentes Compose quando fizer sentido

Documente no /docs:
- architecture.md
- decisions.md
- setup.md
- publishing.md
- testing.md

Em architecture.md explique:
- visão geral dos módulos
- fluxo dos dados
- principais dependências
- rationale técnico

================================================================
11. PUBLICAÇÃO FUTURA NA PLAY STORE
================================================================

Prepare o projeto para futura publicação:
- app name coerente
- ícone placeholder
- tema consistente
- descrição curta e longa em arquivo de apoio, por exemplo docs/store-listing.md
- checklist de publicação
- nota sobre política de privacidade
- nota sobre permissões da câmera e armazenamento
- nota sobre screenshots necessárias

Crie:
docs/store-listing.md
com:
- app title
- short description
- long description
- keywords
- público-alvo
- categoria sugerida
- diferenciais

================================================================
12. TESTES E VALIDAÇÃO
================================================================

Implemente testes mínimos úteis para:
- validação de nomes de documentos
- lógica de exportação
- formatação de datas
- repositórios onde fizer sentido
- alguns use cases
- pelo menos uma cobertura inicial razoável nas partes centrais do domínio

Também crie:
docs/testing.md
explicando:
- o que já é testado
- o que ainda falta
- como rodar testes

================================================================
13. CHANGLOG INICIAL
================================================================

Crie uma primeira entrada no CHANGELOG semelhante a:
- versão inicial do projeto
- arquitetura base
- fluxo de câmera
- importação
- editor inicial
- OCR inicial
- exportação PDF
- histórico local
- documentação inicial
- GitHub Pages inicial
- CI inicial

================================================================
14. EXPERIÊNCIA DE EXECUÇÃO
================================================================

Quero que o repositório fique com cara de projeto real.
Então:

- gere arquivos completos, não só esqueletos vazios
- escreva textos reais e úteis na documentação
- use placeholders quando algo não puder ser gerado agora
- explique TODOs claramente
- não invente integrações inexistentes
- não prometa backend se ele não existir
- não adicione Firebase agora, a menos que seja estritamente necessário
- prefira funcionamento local e offline-first

================================================================
15. UX/UI
================================================================

A interface deve ser:
- moderna
- limpa
- rápida
- minimalista
- com foco no documento
- sem poluição visual

Desejo:
- Home com CTA claro para “Escanear”
- cards de histórico
- fluxo de edição intuitivo
- estados vazios bem feitos
- mensagens de erro amigáveis
- loading states decentes
- onboarding curto e útil
- cores sóbrias

================================================================
16. LIMITES IMPORTANTES
================================================================

NÃO:
- copie branding de terceiros
- use nome parecido demais com apps famosos
- replique identidade visual alheia
- gere código quebrado propositalmente
- faça documentação superficial
- deixe arquivos vazios só para “cumprir tabela”

SIM:
- faça escolhas práticas
- deixe o projeto coerente
- priorize funcionalidade real
- documente limitações
- explique o que falta para produção

================================================================
17. SAÍDA ESPERADA
================================================================

Quero que você aja como se estivesse criando de fato o repositório.

Passos:
1. Defina a estrutura final do projeto
2. Crie os arquivos mais importantes
3. Implemente o núcleo do app
4. Crie a documentação completa
5. Crie o site do GitHub Pages
6. Crie workflows de CI/CD
7. Gere um resumo final com:
   - o que foi criado
   - como rodar
   - o que ainda falta
   - próximos passos prioritários

Não fique preso pedindo confirmação a cada detalhe.
Tome decisões razoáveis e avance.
Quando houver ambiguidade, escolha a opção mais simples, moderna e sustentável.

================================================================
18. EXTRAS DESEJÁVEIS
================================================================

Se couber bem no escopo inicial, adicione:
- importação de múltiplas imagens
- reorder por drag and drop
- favoritos
- fix de rotação
- compressão simples antes de exportar
- seleção de qualidade do PDF
- limpeza de borda preta
- auto enhance básico
- tutorial curto para primeira captura

================================================================
19. ENTREGA FINAL
================================================================

Ao terminar:
- mostre a árvore de arquivos principal
- destaque decisões arquiteturais
- mostre o conteúdo dos arquivos mais importantes
- explique como abrir no Android Studio
- explique como publicar o site no GitHub Pages
- explique como evoluir o projeto para uma versão 1.0 de produção

## 20. PROCESSAMENTO DE IMAGEM E QUALIDADE DE SCAN

O app deve implementar um pipeline de processamento de imagem voltado a documentos, priorizando legibilidade, remoção de sombras, correção geométrica e aparência de scanner real.

Objetivos:
- detectar automaticamente o documento na imagem
- encontrar e permitir ajuste manual dos 4 cantos
- corrigir perspectiva
- melhorar contraste e nitidez do texto
- reduzir sombras e variações de iluminação
- gerar modos de filtro úteis para documentos reais
- preparar a imagem para OCR com alta legibilidade

Pipeline desejado:
1. captura ou importação da imagem
2. conversão para escala de cinza quando necessário
3. redução leve de ruído
4. detecção de bordas
5. detecção de contornos
6. aproximação poligonal para encontrar quadriláteros
7. seleção do contorno mais provável do documento
8. correção de perspectiva
9. pós-processamento da imagem
10. exportação e OCR

Técnicas desejadas:
- Canny ou técnica equivalente para detecção de bordas
- contornos e aproximação poligonal para encontrar a folha
- transformação de perspectiva para “retificar” o documento
- CLAHE ou técnica equivalente para melhorar contraste local
- threshold adaptativo e/ou Otsu para modo documento em preto e branco
- sharpen leve opcional
- remoção de bordas pretas após o warp
- rotação automática quando aplicável

Modos de filtro mínimos:
- Original corrigido
- Documento P&B
- Documento cinza
- Colorido aprimorado
- Recibo / Alto contraste

Requisitos de qualidade:
- o resultado deve parecer um scan limpo, não apenas uma foto com filtro
- texto deve ficar legível mesmo em iluminação imperfeita
- o app deve tentar reduzir sombras e fundos acinzentados
- OCR deve preferencialmente usar a imagem já corrigida e melhorada
- processamento deve priorizar execução local e offline-first

Escopo técnico:
- usar ML Kit Document Scanner quando isso simplificar o fluxo do MVP
- usar processamento adicional local para filtros e melhoria visual
- evitar técnicas avançadas desnecessárias no MVP, como processamento em frequência/Fourier, a menos que haja ganho comprovado em casos reais


Comece agora.


Trabalhe em etapas, mas gere arquivos completos. 
Não responda só com plano. 
Quero implementação real, documentação real e estrutura pronta de repositório.
Quando precisar usar placeholders, marque claramente com TODO.


Agora aprofunde a entrega:
- complete os arquivos incompletos
- melhore README
- deixe o CHANGELOG mais profissional
- complete a página do GitHub Pages
- adicione workflows reais do GitHub Actions
- revise a arquitetura para manter coerência entre módulos
- remova qualquer inconsistência ou arquivo vazio


Antes de implementar, leia este escopo por completo.
Se houver conflito entre simplicidade do MVP e complexidade técnica, priorize o caminho mais simples que entregue:
1. detecção confiável do documento
2. correção de perspectiva
3. filtro com aparência real de scanner
4. OCR funcional
Só use CameraX customizada e processamento mais avançado após a base com ML Kit e pós-processamento local estar estável.