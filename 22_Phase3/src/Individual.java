

public class Individual {

	public char[] chromosome;
	double fitness;

	public Individual(char[] chromosome) {
		this.chromosome = chromosome;
		this.fitness = 0;
	}

	/** 
	 * @return char[]
	 */
	public char[] getChromosome() {
		return chromosome;
	}

	/** 
	 * @param chromosome
	 */
	public void setChromosome(char[] chromosome) {
		this.chromosome = chromosome;
	}

	/** 
	 * @return double
	 */
	public double getFitness() {
		return fitness;
	}

	/** 
	 * @param fitness
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/** 
	 * @return String
	 */
	public String genoToPhenotype() {
		StringBuilder builder = new StringBuilder();
		builder.append(chromosome);
		return builder.toString();
		
	}
	
	//	/** 
	//	 * @return Individual
	//	 */
	//	public Individual clone() {
	//		char[] chromClone = new char[chromosome.length];
	//		for(char i = 0; i < chromClone.length; i++) {
	//			chromClone[i] = chromosome[i];
	//		}
	//		return new Individual(chromClone);
	//	}

}

