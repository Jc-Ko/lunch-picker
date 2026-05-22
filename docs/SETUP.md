# SETUP.md — 로컬 개발 환경 세팅 가이드

> Day 1 세션 중 같이 진행하는 상세 설정 가이드입니다.
> PRE_SETUP_CHECKLIST.md의 사전 설치가 완료된 상태를 가정합니다.

---

## 목차

1. [WSL2 기본 설정](#1-wsl2-기본-설정)
2. [WSL2에 Node.js 설치](#2-wsl2에-nodejs-설치)
3. [Docker Desktop + MySQL 설정](#3-docker-desktop--mysql-설정)
4. [GitHub 저장소 클론](#4-github-저장소-클론)
5. [백엔드 설정 (IntelliJ + WSL)](#5-백엔드-설정-intellij--wsl)
6. [프론트엔드 설정 (VS Code + WSL)](#6-프론트엔드-설정-vs-code--wsl)
7. [Gemini API 키 발급 (Dev C)](#7-gemini-api-키-발급-dev-c)
8. [전체 실행 확인](#8-전체-실행-확인)

---

## 1. WSL2 기본 설정

### 1-1. WSL Ubuntu 터미널 열기
Windows 검색에서 `Ubuntu` 검색 후 실행.
또는 Windows Terminal에서 Ubuntu 탭 선택.

### 1-2. Ubuntu 패키지 업데이트
```bash
sudo apt update && sudo apt upgrade -y
```

### 1-3. 기본 개발 도구 설치
```bash
sudo apt install -y git curl wget unzip build-essential
```

### 1-4. WSL2 메모리 제한 설정 (선택, 권장)
Windows에서 `C:\Users\{사용자명}\.wslconfig` 파일 생성:
```ini
[wsl2]
memory=4GB
processors=2
swap=2GB
```
적용:
```powershell
# PowerShell에서
wsl --shutdown
# 이후 Ubuntu 재시작
```

---

## 2. WSL2에 Node.js 설치

> Claude Code를 사용하는 팀원 또는 프론트엔드 담당자 필수

```bash
# nvm 설치
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash

# 터미널 재시작 또는 적용
source ~/.bashrc

# Node.js LTS 설치
nvm install --lts
nvm use --lts

# 확인
node --version   # v20.x.x
npm --version
```

### Claude Code 설치 (해당자만)
```bash
npm install -g @anthropic-ai/claude-code
claude --version
```

---

## 3. Docker Desktop + MySQL 설정

### 3-1. Docker Desktop WSL 연동 확인
Docker Desktop 실행 후:
- `Settings` → `Resources` → `WSL Integration`
- `Ubuntu-24.04` 토글 **ON** → `Apply & Restart`

WSL Ubuntu에서 확인:
```bash
docker --version
docker compose version
```

### 3-2. docker-compose.yml 확인

프로젝트 루트의 `docker-compose.yml` 파일:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: lunch-picker-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_CHARACTER_SET_SERVER: utf8mb4
      MYSQL_COLLATION_SERVER: utf8mb4_unicode_ci
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d   # 최초 실행 시 sql/ 폴더 자동 실행
    command: >
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
      --default-authentication-plugin=mysql_native_password

volumes:
  mysql_data:
```

> `./sql` 폴더의 `01_schema.sql`, `02_sample_data.sql`이 컨테이너 최초 실행 시 **자동으로 순서대로 실행**됩니다.

### 3-3. MySQL 컨테이너 실행

WSL Ubuntu 터미널에서 프로젝트 루트로 이동 후:

```bash
# 컨테이너 실행 (백그라운드)
docker compose up -d

# 실행 확인
docker compose ps

# 로그 확인 (초기화 완료 확인용)
docker compose logs mysql
```

### 3-4. MySQL 접속 확인

```bash
# 컨테이너 내부 접속
docker exec -it lunch-picker-mysql mysql -u root -prootpassword

# DB 목록 확인
SHOW DATABASES;
# menu_db, ai_db 가 보이면 정상

# 메뉴 데이터 확인
USE menu_db;
SELECT COUNT(*) FROM menus;   -- 20 이면 정상

EXIT;
```

### 3-5. MySQL Workbench 또는 DBeaver 연결 (선택)

Windows에서 GUI 툴로 접근할 때:
- Host: `127.0.0.1`
- Port: `3306`
- User: `root`
- Password: `rootpassword`

> WSL2는 localhost로 Windows와 포트를 공유하므로 별도 설정 없이 연결됩니다.

---

## 4. GitHub 저장소 클론

### 4-1. SSH 키 설정 (WSL Ubuntu)
```bash
# SSH 키 생성
ssh-keygen -t ed25519 -C "your_email@example.com"

# 공개키 출력 후 GitHub에 등록
cat ~/.ssh/id_ed25519.pub
```
GitHub → Settings → SSH and GPG keys → New SSH key → 붙여넣기

### 4-2. 저장소 클론
```bash
# WSL Ubuntu 홈 디렉토리에 클론
cd ~
git clone git@github.com:{org}/lunch-picker.git
cd lunch-picker

# 브랜치 확인
git branch -a
```

### 4-3. 본인 서비스 브랜치 생성
```bash
# 예: Dev A의 경우
git checkout -b feature/menu-service
```

---

## 5. 백엔드 설정 (IntelliJ + WSL)

### 5-1. IntelliJ에서 WSL 경로 열기

IntelliJ IDEA 실행 후:
- `File` → `Open`
- 경로: `\\wsl$\Ubuntu-24.04\home\{username}\lunch-picker\backend`
- 또는 `Open` 대화상자에서 `\\wsl$` 입력 후 탐색

> IntelliJ는 WSL 내부 파일시스템을 네트워크 드라이브처럼 접근합니다.

### 5-2. JDK 설정
- `File` → `Project Structure` → `SDK`
- `+` → `Add JDK` → 설치된 JDK 17 경로 선택
  - Windows 기본 경로: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot`

### 5-3. Gradle 설정 확인
`build.gradle` 열어서 확인:
```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.14'
    id 'io.spring.dependency-management' version '1.1.7'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

### 5-4. application.yml 설정

`src/main/resources/application.yml`:

```yaml
# backend/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/menu_db?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
    username: menu_writer
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: none          # sql 파일로 이미 생성했으므로 none
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

  # ai_db 추가 DataSource (aipicker 도메인용)
  ai-datasource:
    url: jdbc:mysql://localhost:3306/ai_db?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
    username: ai_writer
    password: password

gemini:
  api:
    key: ${GEMINI_API_KEY}    # 환경변수로 주입 (Dev C)
    url: https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent

server:
  port: 8080
```

> `menu_db`는 기본 DataSource로 설정. `ai_db`는 별도 DataSource Bean으로 등록.
> picker 도메인(Dev B)은 menu_db를 읽기 전용으로 사용하므로 별도 설정 불필요.

> ⚠️ `application.yml`은 `.gitignore`에 추가하지 않습니다.
> API 키 등 민감 정보는 IntelliJ Run Configuration의 환경변수로 주입하세요.

### 5-5. IntelliJ Run Configuration 환경변수 설정 (Dev C 필수, 나머지 선택)
- `Run` → `Edit Configurations`
- 해당 Spring Boot 설정 선택
- `Environment variables` 항목에 추가:
  ```
  GEMINI_API_KEY=발급받은키값
  ```

### 5-6. IntelliJ 추천 플러그인
- `Lombok` — 필수
- `Spring Boot` — Spring 지원
- `.env files support` — 환경변수 파일 지원

### 5-7. 백엔드 실행 확인
IntelliJ에서 `Run` 버튼 클릭 후:
```bash
# WSL 또는 Windows PowerShell에서
curl http://localhost:8081/api/menus
# [] 또는 메뉴 목록 JSON 응답 확인
```

---

## 6. 프론트엔드 설정 (VS Code + WSL)

### 6-1. VS Code에서 WSL 연결

방법 1 — WSL 터미널에서 직접 열기 (권장):
```bash
cd ~/lunch-picker/frontend
code .
```
VS Code가 자동으로 WSL Remote 모드로 열립니다.

방법 2 — VS Code에서 연결:
- `F1` → `WSL: Connect to WSL`
- 좌측 하단 `><` 아이콘 → `Connect to WSL`

### 6-2. VS Code WSL 확인
좌측 하단에 `WSL: Ubuntu-24.04` 표시 확인.
이 상태에서 터미널(`Ctrl+\``)을 열면 자동으로 WSL Ubuntu 터미널입니다.

### 6-3. 프론트엔드 의존성 설치

VS Code 터미널(WSL)에서:
```bash
# Vite + React 프로젝트 생성 (최초 1회, 이미 있으면 skip)
npm create vite@latest . -- --template react
npm install

# 필수 패키지 설치
npm install react-router-dom
npm install @tanstack/react-query
npm install zustand
npm install axios
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

### 6-4. Tailwind CSS 설정

`tailwind.config.js`:
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

`src/index.css` 상단에 추가:
```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

### 6-5. Axios 기본 설정

`src/api/axiosInstance.js`:
```javascript
import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export default axiosInstance
```

`.env.local` (프로젝트 루트):
```
VITE_API_BASE_URL=http://localhost:8080
```

### 6-6. 프론트엔드 실행 확인
```bash
npm run dev
# http://localhost:3000 접속 확인
```

`vite.config.js`에서 포트 지정:
```javascript
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
  },
})
```

### 6-7. VS Code 추천 Extension (WSL 환경에 설치)
VS Code WSL 모드에서 Extensions 탭 → 아래 Extension 검색 후 `Install in WSL` 클릭:
- `ESLint`
- `Prettier - Code formatter`
- `Tailwind CSS IntelliSense`
- `ES7+ React/Redux/React-Native snippets`

---

## 7. Gemini API 키 발급 (Dev C)

1. [Google AI Studio](https://aistudio.google.com) 접속 (Google 계정 로그인)
2. 좌측 메뉴 `Get API key` → `Create API key`
3. 발급된 키 복사
4. IntelliJ Run Configuration 환경변수에 `GEMINI_API_KEY` 로 등록 (5-5 참고)

> ✅ 무료 티어: Gemini 2.0 Flash 기준 **하루 1,500회** 요청 가능. 신용카드 불필요.

---

## 8. 전체 실행 확인

### 체크리스트

```bash
# 1. Docker MySQL 실행 확인
docker compose ps
# lunch-picker-mysql   Up   0.0.0.0:3306->3306/tcp

# 2. DB 데이터 확인
docker exec -it lunch-picker-mysql mysql -u root -prootpassword -e "USE menu_db; SELECT COUNT(*) FROM menus;"
# COUNT(*) = 20

# 3. 백엔드 실행 (IntelliJ에서 Run)
curl http://localhost:8080/api/menus        # menu 도메인
curl http://localhost:8080/api/picker/pick  # picker 도메인
curl http://localhost:8080/api/ai/history   # aipicker 도메인

# 4. 프론트엔드 실행 (VS Code 터미널에서 npm run dev)
# 브라우저에서 http://localhost:3001 확인
```

### 자주 발생하는 문제

| 증상 | 원인 | 해결 |
|------|------|------|
| `docker: command not found` | Docker Desktop WSL 연동 미설정 | Docker Desktop → Settings → WSL Integration → Ubuntu ON |
| MySQL 접속 거부 | 컨테이너 초기화 중 | `docker compose logs mysql` 확인 후 30초 대기 |
| `Port 3306 already in use` | 로컬 MySQL 실행 중 | `net stop mysql` (Windows) 또는 기존 MySQL 서비스 중지 |
| IntelliJ에서 WSL 경로 못 찾음 | 경로 형식 오류 | `\\wsl$\Ubuntu-24.04\home\...` 형식으로 입력 |
| `CORS error` | 백엔드 CORS 설정 누락 | `CorsConfig.java`에 프론트 포트 허용 추가 |
| Tailwind 스타일 안 먹힘 | `index.css` import 누락 | `main.jsx`에 `import './index.css'` 확인 |

---

> 세팅 중 문제가 발생하면 혼자 해결하려 하지 말고 바로 PM/PL에게 알려주세요.
> 에러 메시지 전체를 캡처하거나 복사해서 공유해주시면 빠르게 해결할 수 있습니다.
