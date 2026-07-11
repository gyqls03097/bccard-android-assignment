# BCCard Android Assignment

## 개요
Unsplash API 를 활용해 사진을 탐색하고, 마음에 드는 사진을 로컬에 저장(좋아요)하는 기능을 구현
 - 이 기능 외 나머지 요소는 자유롭게 개발

## 주요 화면 및 기능
### 사진 목록 화면
 - 최신 사진 리스트 표시
 - 무한 스크롤
 - 좋아요 토글

### 사진 상세 화면
 - 고해상도 이미지 표시
 - 작가 정보 표시
 - 상세설명 표시
 - 좋아요 토글
 - 진입점 : 목록에서 사진 아이템 클릭

### 좋아요 목록 화면
 - 로컬 DB 에 저장된 사진만 표시
 - 진입점 : 메인 화면 상단 하트 아이콘 클릭

## API
 - Base URL: https://api.unsplash.com/
 - Header: `Authorization`: `Client-ID {acccess_key}`
 - 사진목록: [GET] /photos
 - 사진상세: [GET] /photos/{id}
 - 다운로드: [GET] /photos/{id}/download

## 일정
 - 7월 19일까지


