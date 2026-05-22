# 📋 Day 1 전 사전 설치 체크리스트

> 팀원 여러분, Day 1 세션 **전날까지** 아래 항목을 설치해주세요.
> 설치만 하면 됩니다. 설정은 Day 1에 같이 합니다.
> 막히는 부분 있으면 미리 PM/PL에게 연락 주세요.

---

## ✅ 필수 설치 목록

### 1. WSL2 + Ubuntu 설치
- [ ] **WSL2 활성화** — PowerShell(관리자)에서 실행
  ```powershell
  wsl --install
  ```
- [ ] 재부팅 후 **Ubuntu 24.04 LTS** 설치 확인
  ```powershell
  wsl --install -d Ubuntu-24.04
  ```
- [ ] Ubuntu 최초 실행 후 **username / password 설정** 완료

> ⚠️ 이미 WSL1이 설치되어 있다면 `wsl --set-default-version 2` 실행 후 PM/PL에게 알려주세요.

---

### 2. Docker Desktop 설치
- [ ] [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/) 다운로드 및 설치
- [ ] 설치 후 **Settings → Resources → WSL Integration** 에서 `Ubuntu-24.04` 토글 **ON**
- [ ] Docker Desktop 실행 상태에서 WSL Ubuntu 터미널에서 확인
  ```bash
  docker --version
  docker compose version
  ```

---

### 3. 개발 툴 설치 (Windows)

#### 백엔드 담당
- [ ] **IntelliJ IDEA** (Community 무료 / Ultimate 유료)
  - [다운로드](https://www.jetbrains.com/idea/download/)
- [ ] **JDK 17** 설치
  - [Eclipse Temurin JDK 17](https://adoptium.net/temurin/releases/?version=17) 다운로드
  - 설치 후 확인: `java -version` → `17.x.x` 출력 확인

#### 프론트엔드 담당
- [ ] **VS Code** 설치
  - [다운로드](https://code.visualstudio.com/)
- [ ] VS Code Extension 설치
  - `WSL` (ms-vscode-remote.remote-wsl)
  - `ES7+ React/Redux/React-Native snippets`
  - `Tailwind CSS IntelliSense`
  - `Prettier - Code formatter`

#### 모든 팀원
- [ ] **Git** 설치 (Windows)
  - [다운로드](https://git-scm.com/download/win)
- [ ] **GitHub 계정** 준비 및 PM/PL에게 계정명 공유

---

### 4. AI 코딩 툴 (본인이 쓸 것 하나만 설치)
- [ ] **Cursor** — [cursor.com](https://www.cursor.com/) (VSCode 기반, 추천)
- [ ] **Claude Code** — Node.js 설치 후 `npm install -g @anthropic-ai/claude-code`
- [ ] **GitHub Copilot** (Codex) — VS Code Extension

> 셋 다 설치할 필요 없습니다. 하나만 골라주세요.

---

## ⚠️ 설치 전 확인사항

- Windows 버전: **Windows 10 21H2 이상** 또는 **Windows 11**
  - 확인: `설정 → 시스템 → 정보 → Windows 사양`
- 가상화 활성화 여부 확인 (WSL2 필수)
  - 작업 관리자 → 성능 탭 → CPU → **가상화: 사용** 확인
  - 비활성화 상태면 BIOS에서 Intel VT-x / AMD-V 활성화 필요 → PM/PL에게 연락

---

## 📦 설치 후 확인 명령어

모두 설치했다면 WSL Ubuntu 터미널에서 아래 명령어 실행 후 스크린샷 찍어두세요.
Day 1 세션 시작 전 PM/PL에게 공유해주시면 빠르게 진행할 수 있습니다.

```bash
# WSL Ubuntu 터미널에서 실행
docker --version
docker compose version
git --version
```

```powershell
# Windows PowerShell에서 실행
java -version
node --version   # Claude Code 설치한 경우
```

---

> 설치 중 문제가 생기면 혼자 해결하려 하지 말고 바로 PM/PL에게 연락하세요 🙂
