curl -X POST \
        -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInNpZ25fdHlwZSI6IlNJR04ifQ.eyJhcGlfa2V5IjoiMWNiZjdlNjk0ZTY0NGZkMjg5NjVkMTYzZmFhYzg1NzIiLCJleHAiOjE3NDU2NzE5MjU5NDMsInRpbWVzdGFtcCI6MTc0NTY3MDEyNTk0NX0.C1SepZjUV207_CLzzK8gym51q5vHbKWxv2Xvm7m4Ks0" \
        -H "Content-Type: application/json" \
        -H "User-Agent: Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)" \
        -d '{
          "model":"glm-4",
          "stream": "true",
          "messages": [
              {
                  "role": "user",
                  "content": "1+1"
              }
          ]
        }' \
  https://open.bigmodel.cn/api/paas/v4/chat/completions