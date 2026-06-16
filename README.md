# Consulta de Informações Nutricionais dos Alimentos - Open Food Facts

## Descrição
Aplicativo Android que consulta informações de produtos alimentícios a partir do
código de barras. O usuário digita o código de barras de um produto e o app exibe
nome, marca, quantidade, Nutri-Score e ingredientes, consumindo a API pública do
Open Food Facts. Resolve o problema de consultar rapidamente dados nutricionais e
de composição de um produto sem precisar procurar manualmente.

## API utilizada
- Nome da API: Open Food Facts
- Endpoint utilizado: `https://world.openfoodfacts.org/api/v2/product/{barcode}`
- Exemplo de URL consultada: `https://world.openfoodfacts.org/api/v2/product/3017624010701?fields=product_name,brands,quantity,nutrition_grades,ingredients_text`
- Principais dados retornados: nome do produto, marca, quantidade, Nutri-Score
  (nutrition_grades) e lista de ingredientes.

### Códigos de barras para teste
Produtos com cadastro completo na base para utilizar como exemplo:
- `3017624010701` — Nutella (Ferrero)
- `7622210449283` — Oreo
- `5449000000996` — Coca-Cola

> Observação: a base do Open Food Facts é colaborativa. Produtos regionais ou pouco
> cadastrados podem retornar dados incompletos. Por isso o app exibe "Não informado"
> quando um campo não existe, em vez de quebrar.

## Funcionalidades
- Entrada de dados pelo usuário: código de barras
- Validação de campo vazio
- Consulta à API pública
- Exibição dos dados retornados
- Tratamento básico de erro: sem internet e produto não encontrado

## Tecnologias utilizadas
- Kotlin
- Android Studio
- XML
- Volley (biblioteca de requisição HTTP)
- API pública Open Food Facts

## Permissões utilizadas
O aplicativo utiliza a permissão INTERNET para realizar requisições à API pública.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Como executar o projeto
1. Clonar este repositório.
2. Abrir o projeto no Android Studio.
3. Aguardar a sincronização do Gradle.
4. Executar o app em um emulador ou dispositivo físico.
5. Informar um código de barras válido e realizar a consulta.

## Prints do aplicativo
Vou adicionar pronts da versão final do aplicativo

## Autor
Igor
