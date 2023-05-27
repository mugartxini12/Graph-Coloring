# Group 22 Phase 3 
This repository contains the code for the third phase of project 1 for which an algorithm to most accurately find the chromatic number for different graphs had to be produced.

## Compiling 
- `cd src`
- `javac Main.java`
- `jar -cvmf Group22.mf Group22.jar *.class`

## Running 
`java -jar Group22.jar <Path to Graph file>`

## Structure 
- Source : `/src`
- example Graphs : `/graphs`

## Code 
- `src\Main.java`  contains the configuration of calls to the different Algorithms as well as the code keeping track of the bounds and results
- `src\Graph.java` contains the definition of the graph as well as most coloring algorithms 
- `src\GraphDecomposer.java` decomposes the graph into its subgraphs 
- `src\RingStructure.java` finds a ring structure in the graph 
- `src\Evolution.java` and `src\Individual.java` contain code relating to a Genetic Algorithm
- `src\Tester.java` contains some scripts to test different parts of this code 