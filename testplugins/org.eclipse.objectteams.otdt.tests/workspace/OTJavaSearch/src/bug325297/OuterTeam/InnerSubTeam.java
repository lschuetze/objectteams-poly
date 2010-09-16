team package bug325297.OuterTeam;

protected team class InnerSubTeam extends InnerSuperRole {
	protected class InnerRole playedBy MyBase {
		String concat(String s1, String s2)
		-> String concat(String s1, String s2);
	}
	void test(InnerRole r) {
		r.concat("1", "2");
	}
}