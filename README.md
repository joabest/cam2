# cam2 — recuperação do aplicativo Android

Projeto de recuperação a partir do APK `com.kycflow-7.apk`.

## Estado
- APK original inspecionado: contém `classes.dex`, `AndroidManifest.xml` binário e recursos Android.
- SHA-256 do APK original: `066c60be6e4abadebd38518ecdb6ad2049848d93809f738910a45dfabc17dec1`.
- O APK não é o código-fonte original. Extração de arquivos não equivale à descompilação completa.
- Nenhuma alteração no login, autenticação ou Firebase foi realizada.
- Nenhum APK recompilado foi produzido.

## Próximas etapas
1. Recuperar a estrutura legível usando JADX e APKTool localmente.
2. Identificar a navegação inicial e dependências de autenticação.
3. Reconstruir um projeto Android de testes com funcionalidades locais e sem acesso a serviços protegidos.
4. Validar o build com Gradle e testar em dispositivo.

Por segurança e privacidade, o APK original e os recursos de mídia não foram enviados para este repositório público.
