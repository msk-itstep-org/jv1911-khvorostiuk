# Spring Application

## Установка проекта на локальный компьютер

### Подготовительный этап

1. Зарегистрируйте свой личный аккаунт на [GitHub](https://guthub.com).
1. Установите локально программу `Git` с официального сайта [git-scm.com](https://git-scm.com).
1. Задайте свои имя пользователя и адрес электронной почты, выполнив в `Git Bash` следующие команды:
   ```shell script
   git config --global user.name "<username>"
   git config --global user.email "<email>"
   ```
   где `<username>` - имя, которое Вы указали в профиле вашего аккаунта `GitHub` (не имя аккаунта!),
   а `<email>` - адрес электронной почты, на который Вы зарегистрировали свой аккаунт.

### Настройка SSH

1. Сгенерируйте ключи шифрования, выполнив в `Git Bash` следующую команду:
   ```shell script
   ssh-keygen -t rsa -b 4096 -C "<email>"
   
   > Generating public/private rsa key pair.
   > Enter a file in wich to save the key (/c/Users/<you>/.ssh/id_rsa): [Нажмите Enter]
   > Enter passphrase (empty for no passphrase): [Нажмите Enter]
   > Enter same passphrase again: [Нажмите Enter]
   ```
   В результате в папке вашего пользователя `Windows` будет создана папка `.ssh`,
   в которой будут лежать два файла:
   `id_rsa` - закрытый ключ шифрования (никогда не передавайте его третьим лицам!),
   и `id_rsa.pub` - открытый ключ, который нам еще понадобится.
1. Добавьте ключ шифрования в `SSH Agent`, выполнив в `Git Bash` следующие команды:
   ```shell script
   eval $(ssh-agent -s)
   ssh-add ~/.ssh/id_rsa
   ```
1. Откройте настройки [SSH and GPG keys](https://github.com/settings/keys) в настройках профиля вашего аккаунта `GitHub`.
1. Добавьте свой публичный ключ шифрования, нажав на кнопку `New SSH key`.
   В открывшейся форме заполните:
   `Title` - название ключа (на ваше усмотрение),
   `Key` - открытый ключ шифрования (содержимое файла `id_rsa.pub`).
   Нажмите кнопку `Add SSH key`, чтобы сохранить ключ.

### Установка проекта

1. Склонируйте данный репозиторий на свой локальный компьютер, выполнив в `Git Bash` следующую команду:
    ```shell script
   git clone git@github.com:<account>/jv31818-<lastname>.git
    ```
   где `<account>` - имя вашего аккаунта `GitHub`,
   `<lastname>` - ваша фамилия.
   Т.е. в команде должна быть ссылка SSH на данный репозиторий.
1. Добавьте в локальный репозиторий ссылку на оригинальный, выполнив в `Git Bash` следующую команду
   ```shell script
   git remote add upstream git@github.com:msk-itstep-org/jv31818-<lastname>.git
   ```
   В команде должна быть ссылка SSH на оригинальный репозиторий из аккаунта `msk-itstep-org`.

## Работа с проектом

### Основные команды `Git`

`git status` - информация о текущей ветке.

`git log` - история коммитов текущей ветки. Чтобы выйти из истории, нажмите клавишу `Q`.

`git checkout <branch>` - переключение на ветку `<branch>`.

`git checkout -b <branch>` - создание новой ветки `<branch>` на основе текущей.

`git add -A` - добавление всех локальных изменений в отслеживаемые (готовые к коммиту).

`git commit -m "<message>"` - выполнение коммита (фиксации изменений) с сообщением `<message>`.

`git commit -a -m "<message>"` - то же самое, что и `git add -A` + `git commit -m "<message>"`.

`git pull (origin|upstream) <branch>` - загрузка изменений из ветки `<branch>` удаленного репозитория `origin` или `upstream`.
`origin` - Ваш личный репозиторий-форк. `upstream` - оригинальный репозиторий аккаунта `msk-itstep-org`.

`git push origin <branch>` - отправка локальных коммитов текущей ветки `<branch>` в соответствующую удаленную ветку репозитория `origin`.

`git reset --hard origin/<branch>` - отменить локальные изменения и сбросить текущую ветку до состояния удаленной ветки `<branch>`.

### Выгрузка изменений из оригинального репозитория

```shell script
git checkout master
git pull origin master
git pull upstream master
git push origin master
```
Переключаемся на локальную ветку `master`,
на всякий случай загружаем изменения из удаленной ветки `master` личного репозитория `origin`,
затем выгружаем новые изменения из оригинального репозитория `upstream`,
и отправляем полученные изменения в свой личный репозиторий. 

### Работа над заданием

Задания выдаются в разделе `Issues` оригинального репозитория в аккаунте `msk-itstep-org`.
Для каждого задания создается новая ветка от `master`.
По завершению выполнения задания фиксируем изменения:

```shell script
git checkout master
git checkout -b <branch>

# Выполняем задание #

git commit -a -m "<message>"
git push origin <branch>
```

После этого создаем `Pull Request`.

### Создание `Pull Request`

Скоро будет...