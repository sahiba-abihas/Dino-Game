public class Matrix implements Constants {

	public int rows;
	public int cols;
	public double[][] elements;
	
	public Matrix(int r, int c)
	{
		rows = r;
		cols = c;
		elements = new double[rows][cols];
		
		for(r = 0; r < rows; r++)
			for(c = 0; c < cols; c++)
				elements[r][c] = Math.random()*2 - 1;		
	}
	
	public void abValElements()
	{
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				elements[r][c] = Math.abs(elements[r][c]);	
	}
	
	public void add(Matrix mat)
	{
		if(rows != mat.rows || cols != mat.cols)
			System.out.println("ERROR Matrix Addition, rows/cols don't match.");
		
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				elements[r][c] += mat.elements[r][c];	
	}
	
	public static Matrix AddThese(Matrix mata, Matrix matb)
	{
		if(mata.rows != matb.rows || mata.cols != matb.cols)
			System.out.println("ERROR Matrix Addition (static method), rows/cols don't match.");
		
		Matrix ret = new Matrix(mata.rows, mata.cols);
		
		for(int r = 0; r < mata.rows; r++)
			for(int c = 0; c < mata.cols; c++)
				ret.elements[r][c] = mata.elements[r][c] + matb.elements[r][c];
		
		return ret;
	}
	
	
	public static Matrix ConvertArrayToMatrix(double[] arr) 
	{	
		Matrix ret = new Matrix(arr.length, 1);
		
		for(int r = 0; r < ret.rows; r++)
			ret.elements[r][0] = arr[r];
		
		return ret;
	}
	
	public static double[] ConvertMatrixToArray(Matrix mat)
	{	
		double[] ret = new double[mat.rows];
		
		for(int r = 0; r < mat.rows; r++)
			ret[r] = mat.elements[r][0];
		
		return ret;
	}
	
	public static Matrix MultiplyThese(Matrix mata, Matrix matb)
	{
		if(mata.cols != matb.rows)
		{
			System.out.println("ERROR Matrix Multiplication (static method), rows/cols don't match.");
			return null;
		}
		
		Matrix ret = new Matrix(mata.rows, matb.cols);
		for(int r = 0; r < ret.rows; r++)
			for(int c = 0; c < ret.cols; c++)
			{
				double val = 0;
				for(int n = 0; n < mata.cols; n++)
					val += mata.elements[r][n] * matb.elements[n][c];
				ret.elements[r][c] = val;
			}
		return ret;
	}
	
	public void applySigmoidal()
	{
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				elements[r][c] = 2.0/(1 + Math.exp(-1*elements[r][c])) - 1.0;
	}
	public Matrix copy()
	{
		Matrix ret = new Matrix(rows, cols);
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				ret.elements[r][c] = elements[r][c];
		return ret;
	}
	public static Matrix CopyOf(Matrix mat)
	{
		Matrix ret = new Matrix(mat.rows, mat.cols);
		for(int r = 0; r < mat.rows; r++)
			for(int c = 0; c < mat.cols; c++)
				ret.elements[r][c] = mat.elements[r][c];
		return ret;
	}
	public void mutateElements()
	{
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				if(Math.random() < MUTATERATE)
					elements[r][c] += Math.random()*MUTATEVALUE - MUTATEVALUE/2;
	}
	public static Matrix AvergeThese(Matrix mata, Matrix matb)
	{
		if(mata.rows != matb.rows || mata.cols != matb.cols)
		{
			System.out.println("ERROR in the Averaging Static Method -- rows/cols don't match.");
			return null;
		}
		
		Matrix ret = new Matrix(mata.rows, mata.cols);
		for(int r = 0; r < ret.rows; r++)
			for(int c = 0; c < ret.cols; c++)
				ret.elements[r][c] = (mata.elements[r][c] + matb.elements[r][c])/2;
		
		return ret;
	}
	public static Matrix CombineThese(Matrix mata, Matrix matb)
	{
		if(mata.rows != matb.rows || mata.cols != matb.cols)
		{
			System.out.println("ERROR in the Combining Static Method -- rows/cols don't match.");
			return null;
		}
		
		Matrix ret = new Matrix(mata.rows, mata.cols);
		for(int r = 0; r < ret.rows; r++)
			for(int c = 0; c < ret.cols; c++)
				if(Math.random() < .5)
					ret.elements[r][c] = mata.elements[r][c];
				else
					ret.elements[r][c] = matb.elements[r][c];
		
		return ret;
	}
	
	
	public String toString()
    {
        String ret = "";
        for(int a = 0; a < rows; a++)
        {
            ret += "[ ";
            for(int b = 0; b < cols; b++)
                ret += elements[a][b] + "   ";
            ret += " ]\n";
        }
        return ret;
    }
	
	
	
}


