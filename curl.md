# meals #

Get all meals:

    curl -X GET "http://localhost:8080/topjava/rest/meals"

Create new meal:

    curl -X POST "http://localhost:8080/topjava/rest/meals" \
         -H "Content-Type: application/json" \
         -d '{
               "dateTime": "2025-05-31T19:00:00",
               "description": "created",
               "calories": "111"
             }'

Filter meals

    curl -X GET "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=20:00&endTime=23:00"

Get meal

    curl -X GET "http://localhost:8080/topjava/rest/meals/100003"

Update meal

    curl -X PUT "http://localhost:8080/topjava/rest/meals/100003" \
         -H "Content-Type: application/json" \
         -d '{
               "dateTime": "2025-01-31T20:00:00",
               "description": "updated",
               "calories": "111"
             }'

Delete meal

    curl -X DELETE "http://localhost:8080/topjava/rest/meals/100003"
    