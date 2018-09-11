import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

public class group42 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
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
		int popsize = 25;
		int ran;
		double children[][] = new double[popsize][10];
		for(int i = 0; i <popsize; i++){
			for(int j = 0; j <10; j++){
				ran = rnd_.nextInt(2);
				children[i][j] = ran;
			}
		}
		
		
        // calculate fitness
		System.out.println(findFitness(children[3]));
		
		
		
        while(evals<evaluations_limit_){
            // Select parents
			
            // Apply crossover / mutation operators
            double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
            evals++;
            // Select survivors
        }

	}
	
	public int findFitness(double child[]){
		int findFitness=0;
		for(int i = 0; i < 10; i++){findFitness+=child[i];}
		return findFitness;
	}
	

}
