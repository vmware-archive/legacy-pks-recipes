## Running a test

The following kicks of the load test:

```
./gradlew gatlingRun -DTARGET_URL=http://10.195.43.139:9080 -DSIM_USERS=3 -DDURATION=1
```

Note that DURATION above is in **minutes**