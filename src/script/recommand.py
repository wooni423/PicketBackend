import mysql.connector
import numpy as np
import json
import random
import sys
from sklearn.metrics.pairwise import cosine_similarity

# 데이터베이스 설정
config = {
    'user': 'picket',
    'password': 'picket',
    'host': 'localhost',
    'database': 'picket',
    'raise_on_warnings': True
}

# 커맨드 라인 인자로부터 기준 게시물 ID를 받습니다.
base_post_id = sys.argv[1]
#sys.argv[1]
# 데이터베이스 연결
cnx = mysql.connector.connect(**config)
cursor = cnx.cursor(dictionary=True)

# 기준 게시물의 벡터를 가져옵니다.
query = "SELECT vector FROM Board WHERE board_id = %s"
cursor.execute(query, (base_post_id,))
result = cursor.fetchone()

if result is None or result['vector'] is None:
    print("No vector found for the specified post ID.")
    cursor.close()
    cnx.close()
    sys.exit()

base_post_vector = np.array(json.loads(result['vector'])).flatten()

# 모든 게시물의 벡터를 가져옵니다.
query = "SELECT board_id, vector FROM Board WHERE board_id != %s AND vector IS NOT NULL"
cursor.execute(query, (base_post_id,))

posts_vectors = {}
for row in cursor:
    vector = row['vector']
    if vector:
        loaded_vector = np.array(json.loads(vector)).flatten()
        posts_vectors[row['board_id']] = loaded_vector

# 유사도 계산
selected_vectors = [v for v in posts_vectors.values()]
similarity_scores = cosine_similarity([base_post_vector], selected_vectors)[0]

# 유사도 점수에 따라 게시물을 추천
post_ids = list(posts_vectors.keys())
sorted_posts = sorted(zip(post_ids, similarity_scores), key=lambda x: x[1], reverse=True)

# 랜덤한 개수(4~8)의 게시물을 선택
num_recommendations = random.randint(4, 8)
recommended_posts = sorted_posts[:num_recommendations]

# 추천된 게시물의 제목과 내용을 가져옵니다.
recommended_posts_info = []
for post_id, score in recommended_posts:
    cursor.execute("SELECT title, content FROM Board WHERE board_id = %s", (post_id,))
    post_info = cursor.fetchone()
    if post_info:
        recommended_posts_info.append({
            "board_id": post_id,
            "title": post_info['title'],
            "content": post_info['content'],
            "similarity_score": score
        })

# 추천된 게시물 정보를 JSON 형태로 반환
# print("Recommended posts:")
# print(json.dumps(recommended_posts_info, ensure_ascii=False, indent=2))

recommended_post_ids = [post_id for post_id, _ in recommended_posts[:num_recommendations]]
print(json.dumps(recommended_post_ids))


# 데이터베이스 연결 종료
cursor.close()
cnx.close()