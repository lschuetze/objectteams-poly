team package bug459055.MyTeam;

protected class MyRole {
	public static void targetMethod() {
		// nothing
	}
	
	void sourceMethod() {
		targetMethod();
	}
}