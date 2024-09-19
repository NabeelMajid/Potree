/* Name: Nabeel Majid(C3287060)
 * Seng4500-Assignment1
 * Date 22/08/23
description: Server that generate linkedlist to store taxscale data and connected to Client to for calculate tax.
*/
public class TaxScale implements Comparable<TaxScale>
{
    private int startIncome;
    private int endIncome;
    private int baseTax;
    private int TaxPD;

    public TaxScale()
    {
        startIncome = 0;
        endIncome = 0;
        baseTax = 0;
        TaxPD = 0;

    }
    public TaxScale( Integer startIncome, Integer endIncome, Integer baseTax, Integer TaxPD)
    {
        this.startIncome = startIncome;
        this.endIncome= endIncome;
        this.baseTax=baseTax;
        this.TaxPD = TaxPD;

    }
    //setters

    public void set_startIncome(Integer startIncome)
    {
        this.startIncome=startIncome;
    }
    public void set_endIncome(Integer endIncome)
    {
        this.endIncome=endIncome;
    }
    public void set_baseTax(Integer baseTax)
    {
        this.baseTax=baseTax;
    }
    public void set_TaxPD(Integer TaxPD)
    {
        this.TaxPD=TaxPD;
    }
    public Integer get_startIncome()
    {
        return this.startIncome;
    }
    public Integer get_endIncome()
    {
        return this.endIncome;
    }
    public Integer  get_baseTax()
    {
        return this.baseTax;
    }
    public Integer get_TaxPD()
    {
        return this.TaxPD;
    }
    //gettters


    @Override
    public int compareTo(TaxScale o) {
        // TODO Auto-generated method stub
        if ((o.startIncome > this.endIncome) && (this.endIncome != -1) && (o.endIncome != -1))
        {

            return 1;

        }
        else if((this.endIncome!= -1)&&(o.endIncome != -1)&& (this.startIncome <o.startIncome) && (o.startIncome <= this.endIncome))
        {

            return -1;

        }
        else if((o.startIncome == this.startIncome) && (this.endIncome != -1) && (o.endIncome != -1))
        {

            return 2;
        }
        else
            return 0;

    }

}
