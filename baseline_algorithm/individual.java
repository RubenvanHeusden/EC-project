class Individual{
	double[] genotype;
	Double fitness;
	
	public Individual(double[] genotype, Double fitness) {
		this.genotype = genotype;
		this.fitness = fitness;
	}

	public Individual() {
		this.genotype = new double[10];
		this.fitness = 0.0;
		
	}
}