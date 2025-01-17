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

To run a game using naive values that the programmer thought would work. **This is enough to pass the score of 1000**
```bash
    ./gradlew bootRun --args='--game.mode=naive'
```

To run a game using settings from a previous training result
```bash
    ./gradlew bootRun --args='--game.mode=pretrained'
```

To run full trainging and then run the best player from training. Training parameters can be adjusted in [application.yaml](src/main/resources/application.yaml)
Training was done using a genetic algorithm. Unfortunately ~30min trainings did not yield better results as naive solution and longer trainings were not tried. 
Code is still available and longer trainings can be tried.
```bash
    ./gradlew bootRun     ./gradlew bootRun --args='--game.mode=train'
```
