

import java.util.Random;

import java.util.Arrays;


/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 * 
 * @author Jo Stevens
 * @version 1.0, 14 Nov 2008
 * 
 * @author Alard Roebroeck
 * @version 1.1, 12 Dec 2012
 * 
 */

public class Evolution {
	Graph target;
	static final String TARGET = "HELLO WORLD";
	static int  length;
	int[] normalOrder;
	static char[] alphabet = new char[15];
	static Random randomizer = new Random(System.currentTimeMillis());
	static final boolean DEBUG = false;

	/**
	 * @param args
	 */
	public Evolution(Graph target) {
		this.target = target;
		length = target.vertices;
		normalOrder = new int[length];
		for (int c = 0; c < length; c++) {
			normalOrder[c] = c;
		}

	}

	
	/** 
	 * @return int
	 */
	public int start() {
		int popSize = 1000;
		Individual[] pop = getRandomPop(popSize);
		double mutRate = 0.1;
		return evolve(pop, (int) (popSize * 0.6), mutRate, "stagnant", 500);
	}
	
    
	/** 
	 * @param to
	 * @param from
	 * @param value
	 * @return int
	 */
	public int mapRange(int to, int from, int value) {
        int res = (value / from) * to;
        return res;
    }

    
	/** 
	 * @param arr
	 * @param length
	 * @param seed
	 * @param max
	 * @return int[]
	 */
	public int[] modFYshuffle(int[] arr, int length, char[] seed, int max) {
		int[] res = arr.clone();
		for (int i = 0; i < length - 2; i++) {
            int j = mapRange(length - i, max, seed[i]);
            int tmp = res[i];
            res[i] = res[j];
            res[j] = tmp;
        }
        return res;
    }
	
	/** 
	 * @param popSize
	 * @return Individual[]
	 */
	public static Individual[] getRandomPop(int popSize) {

		for (char c = 0; c <15; c++) {
			alphabet[c] = c;
		}
		
		Random generator = new Random(System.currentTimeMillis());
		Individual[] population = new Individual[popSize];
		// we initialize the population with random characters
		for (int i = 0; i < popSize; i++) {
			char[] tempChromosome = new char[length];
			for (int j = 0; j < length; j++) {
				tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)]; // choose a random letter in the
																					// alphabet
			}
			population[i] = new Individual(tempChromosome);
		}
		return population;
	}

	
	/** 
	 * @param population
	 * @param tournamentSize
	 * @param mutationRate
	 * @param metric
	 * @param goal
	 * @return List<String>
	 */
	public int evolve(Individual[] population, int tournamentSize, double mutationRate, String metric,
			double goal) {
		int chromaticN;

		if (DEBUG) {
			System.out.println("Traning starts with Poulation: ");
			for (Individual individual : population) {
				System.out.println(individual.chromosome);
			}
		}
		population = setPopFitness(population);
		population = getNextGeneration(population, 80, mutationRate);
		int counter = 0;
		//ArrayList<String> metrics = new ArrayList<String>();

		//metrics.add(String.valueOf(population.length));
		//metrics.add(String.valueOf(tournamentSize));
		//metrics.add(String.valueOf(mutationRate));
		//double avgFitness;
		//double maxFitness;
		//double minFitness;
		//avgFitness = getAverageFittness(population);
		//minFitness = getMinFitness(population);
		//maxFitness = getMaxFitness(population);
		chromaticN =(int) getMinFitness(population);
		double fitness = 0;
		while (fitness < goal) {
			population = setPopFitness(population);
			
			if (true && counter % 100 == 0)
				System.out.println("on Gen " + counter + " the best Chromatic Number is : " + chromaticN);
			population = getNextGeneration(population, 80, mutationRate);
			counter++;
			switch (metric) {
				case "avg":
					fitness = getAverageFittness(population);
					break;
				case "max":
					fitness = getMaxFitness(population);
					break;
				case "min":
					chromaticN =(int) getMinFitness(population);
					break;
				case "stagnant":
					
					int thisAvg =(int) getMinFitness(population);
					if (thisAvg == chromaticN) {
						fitness++;
					} else if(thisAvg<chromaticN) {
						fitness = 0;
						chromaticN = (int) getMinFitness(population);
					}

					break;

				default:
					fitness = getAverageFittness(population);
					break;
			}
		}
		//avgFitness = getAverageFittness(population);
		//minFitness = getMinFitness(population);
		//maxFitness = getMaxFitness(population);
		//metrics.add(String.valueOf(avgFitness));
		//metrics.add(String.valueOf(maxFitness));
		//metrics.add(String.valueOf(minFitness));
		//metrics.add(String.valueOf(counter));
		if (DEBUG) {
			System.out.println("Traning completet after " + counter + " Generations ");
			for (Individual individual : population) {
				System.out.println(individual.chromosome);
			}
		}
		return chromaticN;
	}

	/**
	 * Does a full generational step including selection crossover and mutation
	 * 
	 * @param pop
	 * @param tournamentSize
	 * @param mutationRate
	 * @return Individual[]
	 */
	public Individual[] getNextGeneration(Individual[] pop, int tournamentSize, double mutationRate) {
		if (pop[0].getFitness() == 0.0)
			pop = setPopFitness(pop);

		pop = selectPop(pop, tournamentSize);
		pop = mate(pop, pop.length, mutationRate);
		pop = setPopFitness(pop);
		return pop;
	}

	/**
	 * computes the average fitness of the population
	 * 
	 * @param pop
	 * @return double
	 */
	static double getAverageFittness(Individual[] pop) {
		double sum = 0.0;
		for (Individual individual : pop) {
			sum += individual.getFitness();
		}
		return sum / pop.length;
	}

	
	/** 
	 * @param pop
	 * @return double
	 */
	static double getMaxFitness(Individual[] pop) {
		double max = 0.0;
		for (Individual individual : pop) {
			if (individual.getFitness() > max)
				max = individual.getFitness();
		}
		return max;
	}

	
	/** 
	 * @param pop
	 * @return double
	 */
	static double getMinFitness(Individual[] pop) {
		double min = Double.MAX_VALUE;
		for (Individual individual : pop) {
			if (individual.getFitness() < min)
				min = individual.getFitness();
		}
		return min;
	}

	/**
	 * sets the fittnes of the poulation using the relative edit distance as the
	 * fitness
	 * 
	 * @param pop
	 * @return Individual[]
	 */
	public Individual[] setPopFitness(Individual[] pop) {


		for (Individual individual : pop) {
	
			
			int[] order = modFYshuffle(normalOrder, length, individual.chromosome, 15);
			
			double fitness =  target.greedyColor(order);

			individual.setFitness(fitness);
		}
		return pop;
	}

	/**
	 * does tournament selection on a population to generate a new population
	 * populated with a subset of the Individuals in the first population
	 * 
	 * @param pop
	 * @param tournamentSize
	 * @return Individual[]
	 */
	public static Individual[] selectPop(Individual[] pop, int tournamentSize) {
		Individual[] newPop = new Individual[pop.length];
		for (int i = 0; i < pop.length; i++) {
			newPop[i] = turnamentSelection(pop, tournamentSize);
		}
		return newPop;
	}

	/**
	 * Do a tournament of Size tournamentSize on the population
	 * 
	 * @param pop
	 * @param tournamentSize
	 * @return Individual
	 */
	public static Individual turnamentSelection(Individual[] pop, int tournamentSize) {
		Individual best = null;
		Random generator = new Random(System.currentTimeMillis());
		for (int i = 0; i < tournamentSize; i++) {
			Individual cur = pop[generator.nextInt(pop.length)];
			if (best == null || cur.getFitness() < best.getFitness())
				best = cur;
		}
		return best;
	}

	/**
	 * A method to perform crossover and mutation over a population of individuals
	 * 
	 * @param parents1
	 * @param popSize
	 * @param mutationRate
	 * @return Individual[]
	 */
	public static Individual[] mate(Individual[] parents1, int popSize, double mutationRate) {
		String[] parents = new String[parents1.length];
		for (int i = 0; i < parents1.length; i++) {
			char[] x = parents1[i].getChromosome();
			parents[i] = new String(x);
		}

		String newPop[] = new String[popSize];
		for (int i = 0; i < popSize / 2; i++) {
			int index1 = randomizer.nextInt(parents.length);
			int index2 = randomizer.nextInt(parents.length);
			while (index1 == index2) {
				index2 = randomizer.nextInt(parents.length);
			}
			String[] kids = crossover(parents[index1], parents[index2]);
			newPop[2 * i] = kids[0];
			newPop[2 * i + 1] = kids[1];
		}

		for (int i = 0; i < popSize; i++) {
			newPop[i] = mutation(newPop[i], mutationRate);
		}
		Individual[] newPop2 = new Individual[popSize];
		for (int i = 0; i < popSize; i++) {
			newPop2[i] = new Individual(newPop[i].toCharArray());
		}

		return newPop2;
	}

	/**
	 * A method to perform crossover for two Individuals
	 * 
	 * @param individual1
	 * @param individual2
	 * @return String[]
	 */
	public static String[] crossover(String individual1, String individual2) {
		double crossoverChance = randomizer.nextDouble();
		int crossovers;
		if (crossoverChance < 0.2) {
			crossovers = 1;
		} else if (crossoverChance < 0.55) {
			crossovers = 2;
		} else if (crossoverChance < 0.9) {
			crossovers = 3;
		} else {
			crossovers = 4;
		}

		int crossoverIndexes[] = new int[crossovers + 2];
		crossoverIndexes[0] = 0;
		crossoverIndexes[crossovers + 1] = individual1.length();
		for (int i = 1; i < crossovers + 1; i++) {
			int index = randomizer.nextInt(individual1.length());
			// System.out.println(index);
			boolean twice = false;
			for (int j = 1; j < i && !twice; j++) {
				if (crossoverIndexes[j] == index) {
					twice = true;
				}
			}
			if (!twice) {
				crossoverIndexes[i] = index;
			} else {
				i--;
			}
		}
		Arrays.sort(crossoverIndexes);

		String newIndividual1 = "";
		String newIndividual2 = "";
		String tempIndividual = "";

		for (int i = 1; i < crossovers + 2; i++) {
			// get the part between two crossoverpoints to the new individuals
			newIndividual1 = newIndividual1 + individual1.substring(crossoverIndexes[i - 1], crossoverIndexes[i]);
			newIndividual2 = newIndividual2 + individual2.substring(crossoverIndexes[i - 1], crossoverIndexes[i]);

			// switch the two original individuals (because crossoverpoint)
			tempIndividual = individual1;
			individual1 = individual2;
			individual2 = tempIndividual;
		}

		String[] newIndividuals = { newIndividual1, newIndividual2 };
		return newIndividuals;
	}

	/**
	 * @param individual
	 * @param mutationRate
	 * @return String
	 */
	public static String mutation(String individual, double mutationRate) {
		int length = individual.length();
		String individualNew = "";
		for (int i = 0; i < length; i++) {
			double x = randomizer.nextDouble();
			if (x < mutationRate) {
				individualNew = individualNew + alphabet[randomizer.nextInt(alphabet.length)];
			} else {
				individualNew = individualNew + individual.charAt(i);
			}
		}
		return individualNew;
	}
}