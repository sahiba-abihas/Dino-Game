public class NeuralNetwork {
//The number of hidden layers will be fixed (hardcoded): 2 layers
public int numinputs;
public int numhidden1;
public int numhidden2;
public int numoutputs;
public Matrix connectIH1; //The strengths of all of the connections
public Matrix connectH1H2; // between the two levels listed.
public Matrix connectH2O; //<-- EX: The connections from Hidden2 to Outputs
public Matrix biasH1; //The strength of the nodes themselves; how strong
public Matrix biasH2; // the neuron fires (not its connection)
public Matrix biasO; //<-- EX: the strength of the output nodes
public NeuralNetwork(int i, int h1, int h2, int o)
{
	numinputs = i;
	numhidden1 = h1;
	numhidden2 = h2;
	numoutputs = o;
	//In order for the matrix multiplication to work out, you need to build
	// these matrices "backwards"... notice in the first one here that the
	// connection is listed INPUTS to HIDDEN1, but the matrix is
	//constructed
	// with hidden then inputs. Do that for all of them!
	connectIH1 = new Matrix(numhidden1, numinputs);
	connectH1H2 = new Matrix(numhidden2, numhidden1);
	connectH2O = new Matrix(numoutputs, numhidden2);
	//The Bias matrices are each column matrices and represent the strength
	//of
	// each node in that layer.
	biasH1 = new Matrix(numhidden1, 1);
	biasH2 = new Matrix(numhidden2, 1);
	biasO = new Matrix(numoutputs, 1);
	}
	public double[] think(double[] senses)
	{
	Matrix Inputs = Matrix.ConvertArrayToMatrix(senses);
	//With each layer of the brain, multiply the connections by the current
	// layer to get the values of the next layer -- it's really important
	// that you do it in that order (otherwise the matrix math won't
	//work).
	// Then you add in biases for that new level.
	// Finally, we want to "normalize" the values (because after all of
	//those
	// multiplications and additions, the numbers might be scaled totally
	//different
	// from each other) -- so we apply a Sigmoidal Function (a math term
	//for
	// an "S" curve) that squishes all of the values down to -1 to 1.
	Matrix Hidden1 = Matrix.MultiplyThese(connectIH1, Inputs);
	Hidden1.add(biasH1);
	Hidden1.applySigmoidal();
	Matrix Hidden2 = Matrix.MultiplyThese(connectH1H2, Hidden1);
	Hidden2.add(biasH2);
	Hidden2.applySigmoidal();
	Matrix Outputs = Matrix.MultiplyThese(connectH2O, Hidden2);
	Outputs.add(biasO);
	Outputs.applySigmoidal();
	return Matrix.ConvertMatrixToArray(Outputs);
	}
    public NeuralNetwork averageBrains(NeuralNetwork b1, NeuralNetwork b2)
    {
        NeuralNetwork ret = new NeuralNetwork(b1.numinputs, b1.numhidden1, b1.numhidden2, b1.numoutputs);
        
        ret.connectIH1 = Matrix.AvergeThese(b1.connectIH1, b2.connectIH1);
        ret.connectH1H2 = Matrix.AvergeThese(b1.connectH1H2, b2.connectH1H2);
        ret.connectH2O = Matrix.AvergeThese(b1.connectH2O, b2.connectH2O);
        ret.biasH1 = Matrix.AvergeThese(b1.biasH1, b2.biasH1);
        ret.biasH2 = Matrix.AvergeThese(b1.biasH2, b2.biasH2);
        ret.biasO = Matrix.AvergeThese(b1.biasO, b2.biasO);
                
        return ret;
    }
    public NeuralNetwork combineBrains(NeuralNetwork b1, NeuralNetwork b2)
    {
        NeuralNetwork ret = new NeuralNetwork(b1.numinputs, b1.numhidden1, b1.numhidden2, b1.numoutputs);
        
        ret.connectIH1 = Matrix.CombineThese(b1.connectIH1, b2.connectIH1);
        ret.connectH1H2 = Matrix.CombineThese(b1.connectH1H2, b2.connectH1H2);
        ret.connectH2O = Matrix.CombineThese(b1.connectH2O, b2.connectH2O);
        ret.biasH1 = Matrix.CombineThese(b1.biasH1, b2.biasH1);
        ret.biasH2 = Matrix.CombineThese(b1.biasH2, b2.biasH2);
        ret.biasO = Matrix.CombineThese(b1.biasO, b2.biasO);
                
        return ret;
    }
    public void copy(NeuralNetwork nn)
    {
        numinputs = nn.numinputs;
        numhidden1 = nn.numhidden1;
        numhidden2 = nn.numhidden2;
        numoutputs = nn.numoutputs;
        
        connectIH1 = Matrix.CopyOf(nn.connectIH1);
        connectH1H2 = Matrix.CopyOf(nn.connectH1H2);
        connectH2O = Matrix.CopyOf(nn.connectH2O);
        biasH1 = Matrix.CopyOf(nn.biasH1);
        biasH2 = Matrix.CopyOf(nn.biasH2);
        biasO = Matrix.CopyOf(nn.biasO);
    }
    public void mutate()
    {
        connectIH1.mutateElements();
        connectH1H2.mutateElements();
        connectH2O.mutateElements();
        biasH1.mutateElements();
        biasH2.mutateElements();
        biasO.mutateElements();
    }
}
