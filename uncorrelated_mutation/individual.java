class Individual{
	double[] genotype;
	Double fitness;
	double[] std_array; 
			
	public Individual(double[] genotype, Double fitness, double[] std_array) {
		this.genotype = genotype;
		this.fitness = fitness;
		this.std_array = std_array;
	}

	public Individual() {
		this.genotype = new double[10];
		this.fitness = 0.0;
	}
}