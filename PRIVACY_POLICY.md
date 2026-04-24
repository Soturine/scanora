# Política de Privacidade do Scanora

Última atualização: 2026-04-21

## Visão geral

Scanora foi projetado para funcionar com processamento local por padrão. O objetivo do app é permitir captura, revisão, OCR e exportação de documentos sem exigir envio obrigatório para servidores externos.

## Dados processados

O app pode processar localmente:

- imagens capturadas pela câmera;
- imagens importadas da galeria;
- texto reconhecido por OCR;
- metadados locais como título, tags, favoritos e datas.

## Onde os dados ficam

No MVP atual:

- histórico, metadados e cópias locais das páginas ficam no armazenamento local do app;
- imagens recebidas de scanner, galeria ou câmera podem ser copiadas para armazenamento interno para evitar dependência de URIs temporárias;
- arquivos exportados são gerados localmente no dispositivo;
- arquivos temporários de processamento podem existir no cache do app por tempo limitado;
- uma rotina leve com `WorkManager` remove arquivos antigos de cache/exportação temporária.

## Permissões usadas

- `CAMERA`: necessária para a captura manual com CameraX.

Importação pela galeria usa o seletor moderno do Android/ML Kit quando aplicável e evita solicitar permissões amplas de armazenamento no MVP.

## OCR e scanner guiado

- O OCR usa ML Kit no dispositivo.
- O scanner guiado usa ML Kit Document Scanner e pode depender de componentes do Google Play services para disponibilizar a experiência completa.
- Mesmo quando bibliotecas do Google são usadas, o objetivo funcional do app continua sendo processar o conteúdo localmente no aparelho.

## Compartilhamento e exportação

- O app exporta PDF, JPG e PNG localmente.
- Quando você escolhe compartilhar, o arquivo selecionado é entregue ao app de destino por meio do mecanismo padrão de compartilhamento do Android.
- O Scanora não faz upload obrigatório automático desses arquivos.

## O que o app não faz neste MVP

- não exige login;
- não cria perfil remoto;
- não sincroniza com nuvem;
- não envia documentos automaticamente a backend próprio.

## Mudanças futuras

Caso integrações futuras sejam adicionadas, como sync opcional, backup remoto ou analytics, esta política deverá ser atualizada antes da publicação da mudança.
