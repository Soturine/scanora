# Escopo do Scanora

## Fonte principal desta rodada

Este arquivo é a referência de produto para a versão `v0.2.2`.

A direção da rodada é consolidar o Scanora como um app em que o fluxo principal é o scanner rápido do Google/ML Kit Document Scanner. O pipeline manual continua existindo, mas deve ser entendido e apresentado como fallback editável para casos em que a pessoa precisa controlar captura, crop, filtros ou importação página por página.

## Estratégia de produto

O Scanora não deve competir com o scanner do Google no que ele já faz melhor. O valor principal do app fica na experiência ao redor do scan:

- entrada rápida pela Home;
- lote local criado a partir do resultado do scanner;
- revisão clara das páginas;
- ajuste manual quando necessário;
- OCR legível e copiável;
- exportação local em PDF, JPG ou PNG sem excesso de opções;
- histórico local simples, pesquisável e privado.

## Verdades atuais

- O scanner do Google entrega crop e resultado final melhores na maioria dos cenários reais.
- O auto crop manual/importado é útil como ponto de partida, mas ainda não deve ser vendido como caminho principal.
- URIs temporárias de scanner/galeria precisam ser copiadas para armazenamento interno antes de entrar no lote.
- O fluxo manual precisa continuar funcional para capturas próprias, fotos difíceis, cadernos e correções finas.
- A Home deve apontar primeiro para `Escanear rápido` e só depois para `Ajuste manual`.

## Escopo obrigatório da v0.2.2

### 1. Scanner rápido como caminho principal

- `Escanear rápido` é o CTA principal da Home.
- `GmsDocumentScanner` é o fluxo padrão.
- A importação de galeria deve ser permitida pelo scanner do Google quando o dispositivo oferecer suporte.
- O resultado do scanner deve criar lote local, aparecer no histórico e abrir direto na revisão.

### 2. Manual como fallback editável

- CameraX e importação manual continuam disponíveis.
- A Home deve apresentar esse caminho como `Ajuste manual`, `Modo manual` ou equivalente.
- A tela manual não deve disputar protagonismo com o scanner rápido.

### 3. Persistência de imagens de entrada

- Imagens vindas de scanner, galeria ou câmera manual devem ser copiadas para armazenamento interno estável antes de serem salvas no Room.
- Preview, filtros, OCR e exportação não podem depender de URI efêmera.
- Processamento pesado deve seguir fora da main thread.

### 4. Home mais direta

- Menos texto simultâneo.
- Um CTA principal forte.
- Histórico local claro.
- Modos e importação manual em área secundária.

### 5. OCR útil

- ML Kit Text Recognition continua sendo o motor.
- A tela deve priorizar leitura e cópia prática.
- `Copiar tudo` deve estar evidente.
- Blocos de texto devem ser apresentados de forma mais organizada quando o reconhecimento retornar blocos úteis.

### 6. Exportação com disclosure progressivo

- Formato vem primeiro.
- Qualidade aparece apenas para PDF.
- Sucesso e erro precisam ser curtos.
- Salvar, abrir e compartilhar devem ficar óbvios.

## Entregue na v0.2.2

- Home reorganizada para concentrar o topo em `Escanear rápido`.
- Manual/importação reposicionados em `Ajuste manual`.
- Scanner rápido segue com galeria habilitada via `GmsDocumentScannerOptions`.
- Resultado de scanner, galeria e CameraX passa a ser copiado para `filesDir/scan-sources` antes de criar o lote.
- Títulos de lote indicam a origem: `Scan rápido`, `Modo manual` ou `Importação manual`.
- Tela manual deixou de oferecer um atalho concorrente para o scanner guiado.
- OCR ganhou `Copiar tudo` no rodapé, feedback de cópia e blocos separados de leitura.
- Exportação ganhou feedback curto de sucesso ao concluir.

## Fora do escopo desta rodada

- Reescrever o pipeline local de crop.
- Adicionar OpenCV, backend, sync, analytics ou bibliotecas pesadas.
- Prometer OCR perfeito.
- Gerar pipeline de assinatura Play Store.
- Trocar a modularização atual.

## Roadmap imediato após v0.2.2

- QA visual em dispositivo real.
- Capturas públicas oficiais.
- Testes instrumentados para scanner/revisão/OCR/exportação.
- Limpeza futura de arquivos fonte órfãos quando scans forem removidos.
- Melhorias incrementais no crop manual para cenários de caderno, espiral e fundo poluído.
