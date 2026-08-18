public class FaturaService{
    public void Historico(String valor){
        switch(){
            case "pix":
            case "boleto": new Boleto().pagar(valor);break;
            case "cartao": new Cartao().pagar(valor);break;
        }
    }
}