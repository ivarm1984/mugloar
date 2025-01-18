# Dragons of Mugloar client

## Getting Started

To build the project and run tests:

```bash
    ./gradlew build
```

To run a game with random player
```bash
     ./gradlew bootRun --args='--game.mode=random'
```

To run a game using values that the programmer thought would work. **This is usually enough to pass the score of 1000**
```bash
    ./gradlew bootRun --args='--game.mode=naive'
```

To run a game using settings from a previous training result.
Unfortunately ~30min trainings did not yield better results as naive solution and longer trainings were not tried.
```bash
    ./gradlew bootRun --args='--game.mode=pretrained'
```

To run full training and then run the best player from training. Training parameters can be adjusted in [application.yaml](src/main/resources/application.yaml).
Training is done using a genetic algorithm.  
```bash
    ./gradlew bootRun --args='--game.mode=train'
```
