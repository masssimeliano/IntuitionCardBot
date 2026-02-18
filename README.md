# Intuition Card Telegram Bot 🃏

**Intuition Card Bot** is a simple Telegram bot designed to train and test human intuition using a standard deck of 52 playing cards.  
The idea is minimalistic: intuition, cards, instant feedback, and personal progress tracking.

---

## 🎯 Concept

Each round, the bot randomly selects a card from a standard 52-card deck.  
The user tries to **guess the card intuitively**, choosing from different difficulty modes.

The game can be played an unlimited number of times, focusing purely on intuition rather than memorization or strategy.

---

## 🎮 Game Modes

The user can choose one of the following modes:

1. **Color Mode**
    - Guess only the color of the card:
        - 🔴 Red
        - ⚫ Black

2. **Suit Mode**
    - Guess the suit of the card:
        - ♠ Spades
        - ♥ Hearts
        - ♦ Diamonds
        - ♣ Clubs

3. **Rank Mode**
    - Guess the rank of the card (without suit):
        - A, K, Q, J, 10, 9, …, 2

4. **Full Card Mode** (Hardest)
    - Guess the exact card including both rank and suit  
      _(e.g. Queen of Hearts, 10 of Spades)_

---

## 🧠 Gameplay Flow

1. The user starts the bot
2. Selects a game mode
3. Makes an intuitive choice via inline buttons
4. The bot reveals the actual card
5. The bot shows whether the guess was correct or not
6. The user can immediately play again or change the mode

The interaction is intentionally simple and fast.

---

## 📊 Statistics & Progress Tracking

The bot stores **personal statistics for each user**, including:

- Total number of attempts
- Number of correct guesses
- Accuracy percentage

Statistics are available for different time periods:

- Today
- Yesterday
- Last week
- Last month
- All time

This allows users to track progress over time and compare intuition performance across different periods.

---

## 🛠 Technical Overview

- **Language:** Java + Spring Boot
- **Telegram API:** Telegram Bots API for Java
- **Bot Type:** Long Polling Bot
- **Database:** PostgreSQL
- **Persistence:** All game results and statistics are stored in the database
- **Architecture:** Clean separation of handlers, services, repositories, and domain models

The bot works entirely on top of a database-backed architecture, ensuring that all user data and statistics are safely persisted.

---

## 🚀 Deployment

The bot can be deployed to any server or cloud service that supports Java and PostgreSQL.

Possible deployment options include:
- VPS / dedicated server
- Cloud providers
- Docker-based environments

All stored data can be migrated or redeployed without loss.

---