# AstroStack

![Minecraft](https://img.shields.io/badge/Minecraft-1.21+-green?style=flat&logo=minecraft)
![Java](https://img.shields.io/badge/Java-21+-orange?style=flat&logo=openjdk)
![Paper](https://img.shields.io/badge/Paper-API-blue?style=flat)
![LiteCommands](https://img.shields.io/badge/LiteCommands-blue?style=flat)
![Lombok](https://img.shields.io/badge/Lombok-red?style=flat)

AstroStack — это плагин для серверов Minecraft Paper, который позволяет увеличивать максимальный размер стака для определенных предметов сверх стандартных ограничений Minecraft.

## Возможности

- **Настраиваемый размер стаков**: Установка пользовательского максимального размера стака для указанных материалов (до 64)
- **Фильтрация материалов**: Настройка того, какие материалы должны иметь измененный размер стака

## Требования

- **Версия Minecraft**: 1.21+
- **Программное обеспечение сервера**: Paper (PaperMC)
- **Версия Java**: 21+

## Установка

1. Скачайте последнюю версию AstroStack
2. Поместите файл `.jar` в папку `plugins` вашего сервера
3. Перезапустите сервер или используйте менеджер плагинов для загрузки плагина
4. Плагин автоматически создаст файл конфигурации по умолчанию

## Конфигурация

Плагин создает файл `config.yml` в директории `plugins/AstroStack/`:

```yaml
max-stack-size: 64

enabled-materials:
  - POTION
  - SPLASH_POTION
  - LINGERING_POTION
```

### Параметры конфигурации

- **max-stack-size**: Максимальный размер стака для разрешенных материалов (1-64)
- **enabled-materials**: Список материалов, для которых должен быть изменен размер стака

### Добавление материалов

Чтобы добавить больше материалов, просто добавьте их в список `enabled-materials`, используя их названия материалов Minecraft:

```yaml
enabled-materials:
  - POTION
  - SPLASH_POTION
  - LINGERING_POTION
  - ENDER_PEARL
  - SNOWBALL
  - EGG
```

Названия материалов можно найти в [документации Spigot Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).

## Разработка

### Сборка из исходного кода

1. Клонируйте репозиторий
2. Убедитесь, что у вас установлена Java 21+
3. Выполните `./gradlew build` (Unix) или `gradlew.bat build` (Windows)
4. Скомпилированный плагин будет находиться в `build/libs/`

### Настройка среды разработки

Проект использует:
- **Gradle**: Система сборки
- **Paper API**: API сервера Minecraft
- **Lombok**: Генерация кода для геттеров/сеттеров

Для запуска сервера разработки:
```bash
./gradlew runServer
```

### Структура проекта

```
src/main/java/dev/twice/astrostack/
├── StackPlugin.java          # Главный класс плагина
├── config/
│   └── ConfigManager.java    # Обработка конфигурации
├── listener/
│   └── InventoryListener.java # Слушатели событий
└── service/
    └── StackService.java     # Основная логика стакинга
```

## Технические детали

- Использует современный API DataComponent от Paper для изменения размера стака
- Эффективно обрабатывает инвентари, используя Java Streams
- Реализует правильные приоритеты событий для избежания конфликтов
- Использует EnumSet для оптимальной производительности поиска материалов

## Поддержка

Если у вас возникли проблемы или вопросы:
1. Проверьте правильность конфигурации
2. Убедитесь, что используете Paper 1.21+
3. Проверьте логи сервера на наличие сообщений об ошибках
4. Создайте issue в репозитории проекта
