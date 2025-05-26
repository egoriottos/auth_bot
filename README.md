# 🤖 Telegram Auth Bot

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1-green)
![Telegram API](https://img.shields.io/badge/Telegram%20Bot%20API-6.9.7-brightgreen)

Бот для безопасной аутентификации пользователей через Telegram WebApp с отображением профиля.

## ✨ Особенности

- 🔐 Аутентификация через Telegram WebApp
- 📊 Отображение профиля пользователя
- 🛡️ Валидация данных через HMAC-подпись
- 🌐 Поддержка HTTPS через ngrok/localtunnel
- 🐳 Запуск базы данных через docker-compose
- 💾 Хранение пользователей в PostgreSQL через Spring Data JPA
- Для установления HTTPS включал VPN и использовался `ngrok http 8080`, предварительно его установив.  
  Можно вместо ngrok использовать `localtunnel` (работает без VPN)
- Перед каждым запуском нужно в `application.yml` прописывать новый сгенерированный URL — другого решения не нашёл

## 🛠 Технологии

- **Backend**: 
  ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?logo=spring&logoColor=white)
  ![Thymeleaf](https://img.shields.io/badge/-Thymeleaf-005F0F?logo=thymeleaf&logoColor=white)
  ![Spring Data JPA](https://img.shields.io/badge/-Spring%20Data%20JPA-blueviolet)
  ![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-336791?logo=postgresql&logoColor=white)
  ![Docker](https://img.shields.io/badge/-Docker-2496ED?logo=docker&logoColor=white)
