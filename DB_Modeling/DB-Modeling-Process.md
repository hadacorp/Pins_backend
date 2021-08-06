# Pins DB Modeling Process

### 7/25
 * 주 기능 테이블인 유저와 핀, 게시판 부분 v1.0 Erd 작성
 ![v1.0-Erd](./Erd-image/Pins-v1.0.png)

 * 수정 필요 부분
    - 커뮤니티 핀과 모임핀에서 참여 유저들 관리 테이블을 어떻게 관리 해야 하는가?, 유저 테이블과의 관계는 어떻게 설정 해야 하는가?
### 7/25
 * v1.0 수정 필요 부분 수정 
 ![v1.0-Erd](./Erd-image/Pins-v2.0.png)
### 8/05
 * date& time 합치는게 좋은듯
 * location은 longitude, latitude로 나누기 
 * regRegNum -> resRedNum
### 8/06
 * Storypin에서 like 부분 user와 엮어 many to one 으로 수정(양방향)
 * 