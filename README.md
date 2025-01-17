# Dragons of Mugloar client

## Getting Started

To build the project and run tests:

```bash
    ./gradlew build 
```

To run a game with random player

```bash
    ./gradlew bootRun -Dgame.mode=random
```

To run a game using naive values that the programmer thought would work. **This is enough to pass the score of 1000**
```bash
    ./gradlew bootRun -Dgame.mode=naive
```

To run a game using settings from a previous training result
```bash
    ./gradlew bootRun -Dgame.mode=pretrained
```

To run full trainging and then run the best player from training. Training parameters can be adjusted in [application.yaml](src/main/resources/application.yaml)
```bash
    ./gradlew bootRun -Dgame.mode=train
```
