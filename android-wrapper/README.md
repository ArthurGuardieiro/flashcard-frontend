# Flashcard App Android Wrapper

Este projeto é um wrapper Android para o servidor Ktor de flashcards. Ele encapsula o servidor web em um aplicativo Android, permitindo que você execute o servidor diretamente no seu dispositivo móvel.

## Estrutura do Projeto

- `app/`: Contém o código do aplicativo Android
- `ktor-module/`: Referência para o projeto Ktor original (pasta `front`)

## Compilando o APK

Para compilar o projeto e gerar um APK, você pode usar os seguintes comandos:

### Usando linha de comando

```bash
# Navegue até o diretório do wrapper Android
cd front/android-wrapper

# Compile um APK de debug
./gradlew assembleDebug

# O APK gerado estará em app/build/outputs/apk/debug/app-debug.apk
```

### Usando o Android Studio

1. Abra o projeto Android Studio selecionando o diretório `front/android-wrapper`
2. Aguarde o Android Studio sincronizar o projeto
3. Selecione "Build > Build Bundle(s) / APK(s) > Build APK(s)"
4. O APK será gerado e você será notificado quando o processo for concluído

## Instalando o APK

1. Transfira o APK para seu dispositivo Android
2. No dispositivo, navegue até o local do arquivo APK e toque nele
3. Siga as instruções para instalá-lo (pode ser necessário habilitar a instalação de fontes desconhecidas)

## Usando o Aplicativo

Uma vez instalado:

1. Inicie o aplicativo a partir do drawer do seu dispositivo
2. O servidor será iniciado automaticamente e você verá o status na tela
3. Você pode acessar as APIs do servidor a partir de:
   - Dispositivo local: http://localhost:8081
   - Outros dispositivos na mesma rede: http://[endereço-ip-do-seu-dispositivo]:8081

## Notas

- O servidor usa a porta 8081 por padrão (diferente da porta 8080 usada pelo backend)
- O aplicativo precisa permanecer aberto para que o servidor continue funcionando
- Certifique-se de que seu dispositivo tem permissões de rede adequadas 