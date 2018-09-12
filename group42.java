import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

public class group42 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	int fittest = 0;
	int secondfittest= 0;
	int fittestind=-1; int secondfittestind=-1;
	int popsize = 10;
	
	
	double child1[] = new double[10];
	double child2[] = new double[10];
	
	public group42()
	{
		rnd_ = new Random();
	}
	
	public static void Main(){
		
		        System.out.println("test1");

	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
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
    
	public void run()
	{
		// Run your algorithm here
        System.out.println("test3");
        int evals = 0;
        // init population
		int ran;
		double children[][] = new double[popsize][10];
		for(int i = 0; i <popsize; i++){
			for(int j = 0; j <10; j++){
				ran = rnd_.nextInt(2);
				children[i][j] = ran;
			}
		}
		
		
        // calculate fitness
		
		
		
		
		
        while(evals<evaluations_limit_){
            // Select parents
			findFittest(children);
            // Apply crossover / mutation operators
			crossover(children[fittestind],children[secondfittestind],5);
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child1);
            evals++;
            // Select survivors
			int rip = leastFit(children);
			children[rip] = child1;
			rip = leastFit(children);
			children[rip] = child2;
        }
		for(int i=0; i < popsize; i++){
			
			System.out.println("Fitness of child " + i + ":" + findFitness(children[i]));

		}

	}
	
	public int findFitness(double child[]){
		int fitness=0;
		for(int i = 0; i < 10; i++){fitness+=child[i];}

		return fitness;
	}
	public void findFittest(double children[][]){

		for(int i=0; i < popsize; i++){
			int found = findFitness(children[i]);
			if( found > fittest){fittestind = i;fittest = found;} else if (found > secondfittest){secondfittestind = i; secondfittest = found;}
		}
		
	}
	
	public int leastFit(double children[][]){
		int least = 10;
		int leastind = -1;
		for(int i=0; i < popsize; i++){
			int found = findFitness(children[i]);
			if( found < least){leastind = i;least = found;}
		}
		return leastind;
	}
	
	public void crossover(double fittest[], double secondfittest[], int crosspoint){

		for(int i = 0; i< 10; i++){
			if(i < crosspoint){
				child1[i] = fittest[i];
				child2[i] = secondfittest[i];
			}
			else{
				child1[i] = secondfittest[i];
				child2[i] = fittest[i];
			}
		}
	}
	
	

}
