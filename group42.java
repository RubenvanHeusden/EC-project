import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;


public class group42 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
    int populationSize = 100;
    int offspringSize = 50;
	double e = Double.parseDouble(System.getProperty("var1"));	
	
	
	public group42()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algorithms random process
		rnd_.setSeed(seed);
	}

	public static void main(String[] args) {
		
		
	}
	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
	
	
	// Formula for linear probability of parenthood selection based
	// on rank of individual (EC Book p.82)
	public double parent_prob(int rank, int pop_length, double s) {
		return (2-s) / (pop_length) + 2*rank*(s - 1) / (pop_length*(pop_length-1));
	}
	
	
	//
	//
	public Individual[] parent_selection(Individual[] population){
		double[] chances = new double[population.length];
		double[] distribution = new double[population.length];
		// set s parameter for selection
		double s = 1.5;
		Arrays.sort(population, (a, b) -> Double.compare(a.fitness, b.fitness));
		for(int i =0; i<population.length;i++) {
			chances[i] = parent_prob(i, population.length, s);
			distribution[i] = Arrays.stream(chances, 0, i+1).sum();
		}
		
		Individual[] mating_pool = new Individual[offspringSize];
		
		int current_member = 0;
		while (current_member < offspringSize) {
			double r = rnd_.nextFloat();
			int i = 0;
			while(distribution[i] < r ) {
				i+=1;
			}
			mating_pool[current_member] = new Individual(population[i].genotype, population[i].fitness,
					population[i].std_array);
			current_member+=1;
		}	
	return mating_pool;
	}
	
	
	
	
	public Individual[] survivor_selection(Individual[] population, Individual[] children) {
		Individual[] survivors = new Individual[population.length+children.length];
		for (int i  = 0;i<population.length;i++) {
			survivors[i] = population[i];
		}
		for (int j = 0; j<children.length;j++) {
			survivors[population.length+j] = children[j];
			
		}
		
		//Arrays.sort(survivors, (a, b) -> Double.compare(a.fitness, b.fitness));
		//return Arrays.copyOfRange(survivors, survivors.length-(population.length+1), survivors.length-1);
		
		Arrays.sort(survivors, (a, b) -> Double.compare(b.fitness, a.fitness));
		return Arrays.copyOfRange(survivors, 0, population.length);
		
	}
	
	
	
	
	
	public Individual mutation(Individual individual){
		// parameters as specified in page 60 of the ec book.
		double tau = 1/Math.sqrt(2*individual.genotype.length);
		double tau_prime = 1/Math.sqrt(2*Math.sqrt(individual.genotype.length));

		for (int x = 0; x<individual.genotype.length; x++){
			individual.std_array[x] = Math.max(individual.std_array[x]*Math.exp(tau_prime*rnd_.nextGaussian()+
					tau*rnd_.nextGaussian()), e);

			
			double allele = individual.genotype[x];
			double mutated_value = allele + individual.std_array[x]*rnd_.nextGaussian();
			individual.genotype[x] = mutated_value;
		}
		return individual;
	}
		
	public Individual[] recombination(Individual parent1, Individual parent2){
		// NO GUARANTEE OF CROSSOVER !!!
		// simple one point crossover with two parents and two children
		int crossover_point = rnd_.nextInt(parent1.genotype.length);
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		for(int i = 0; i < crossover_point;i++){
			child1.genotype[i] = parent1.genotype[i];
			child2.genotype[i] = parent2.genotype[i];
		}
		
		for(int j = crossover_point; j < child1.genotype.length;j++){
			child1.genotype[j] = parent2.genotype[j];
			child2.genotype[j] = parent1.genotype[j];
			
		}
		child1.std_array = Arrays.copyOf(parent1.std_array, 10);
		child2.std_array = Arrays.copyOf(parent2.std_array, 10);
		Individual child1_mut = mutation(child1);
		Individual child2_mut = mutation(child2);

		return new Individual[] {child1_mut, child2_mut};
	}
	
		
	public void run()
	{
		// Run your algorithm here
        int evals = 0;
        // init population
		Individual[] population = new Individual[populationSize];
		for(int x = 0; x<populationSize;x++){
			double[] gen_type = new double[10];
			// used for the array of standard deviations
			double[] std_array = new double[10];
			for(int y = 0; y<10;y++){
				gen_type[y] = -5 + rnd_.nextDouble() * (5 + 5);	
				std_array[y] = Math.max(rnd_.nextGaussian(), e);
				
			}
			population[x] = new Individual(gen_type, 0.0, std_array);
			population[x].fitness = (double) evaluation_.evaluate(population[x].genotype);
			evals++;
			
		}
		
		
        while(evals<evaluations_limit_){
    	
    		Individual[] offspring = new Individual[offspringSize];
    		Individual[] parents =  parent_selection(population);
    		
    		// fill the offspring array with the offspring from the recombination operator
    		for(int j = 0; j< parents.length;j+=2) {
    			Individual[] children  = recombination(parents[j], parents[j+1]);
    			
    			children[0].fitness =  (double) evaluation_.evaluate(children[0].genotype);
    			offspring[j] = children[0];
    			evals++;
    			
    			children[1].fitness =  (double) evaluation_.evaluate(children[1].genotype);
    			offspring[j+1] = children[1];
    			evals++;	
    		}
    		// the survivor selection takes the offspring and old population and returns
    		// the new population, selected on fitness
    		population = survivor_selection(population, offspring);
        }

	}
}