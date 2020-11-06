import com.botticelli.bot.Bot;
import com.botticelli.bot.request.methods.MessageToSend;
import com.botticelli.bot.request.methods.types.*;

import java.util.ArrayList;
import java.util.List;

public class BotSpesa extends Bot {
    List<ListaDellaSpesa> listeSpesa = new ArrayList<>();
    public BotSpesa(String token) {
        super(token);
    }

    @Override
    public void textMessage(Message message) {

        ListaDellaSpesa listaUtente = getUserListaSpesa(message.getFrom());
        switch (listaUtente.getStatoUtente()) {
            case NORMALE:
                ComandoListaSpesa comandoListaSpesa = ComandoListaSpesa.fromString(message.getText().toLowerCase());

                switch (comandoListaSpesa) {
                    case START:
                        inviaMessaggioBenvenuto(message.getFrom());
                        break;
                    case LISTA:
                        sendMessage(new MessageToSend(message.getFrom().getId(), listaUtente.toString()));
                        break;
                    case AGGIUNGI:
                        listaUtente.setStatoUtente(StatoUtente.AGGIUNGIPRODOTTO);
                        sendMessage(new MessageToSend(message.getFrom().getId(), "Inviami quale prodotto aggiungere scrivendo nome prodotto-quantità oppure solo il prodotto"));
                        break;
                    case RIMUOVI:
                        listaUtente.setStatoUtente(StatoUtente.RIMUOVIPRODOTTO);
                        sendMessage(new MessageToSend(message.getFrom().getId(), "Scrivimi il numero di prodotto che vuoi rimuovere"));
                        break;
                    case ERRORE:
                        sendMessage(new MessageToSend(message.getFrom().getId(), "Comando non valido"));
                        break;
                }
                break;
            case AGGIUNGIPRODOTTO:
                String text = message.getText();
                String[] parametri = text.split("-");
                if (parametri.length == 1)
                    listaUtente.addItem(parametri[0]);
                else
                    listaUtente.addItem(parametri[0], Integer.valueOf(parametri[1]));
                sendMessage(new MessageToSend(message.getFrom().getId(), "Prodotto aggiunto!"));
                break;
            case RIMUOVIPRODOTTO:
                if(listaUtente.removeItem(Integer.valueOf(message.getText())))
                    sendMessage(new MessageToSend(message.getFrom().getId(), "Prodotto rimosso!"));
                else
                    sendMessage(new MessageToSend(message.getFrom().getId(), "Indice non valido"));
                break;
        }
    }
    private void inviaMessaggioBenvenuto(User user){
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        line.add(new KeyboardButton("Lista della spesa"));
        keyboard.add(line);
        line = new ArrayList<>();
        line.add(new KeyboardButton("Aggiungi"));
        line.add(new KeyboardButton("Rimuovi"));
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);

        MessageToSend mts = new MessageToSend(user.getId(), "Benvenuto, usa questo bot per gestire la spesa");
        mts.setReplyMarkup(replyKeyboard);
        sendMessage(mts);
    }

    private void inviaMessaggioTastiera(User user){
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        line.add(new KeyboardButton("\uD83D\uDEF4"));
        line.add(new KeyboardButton("\uD83D\uDEF5"));
        line.add(new KeyboardButton("\uD83C\uDFCD"));
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);

        MessageToSend mts = new MessageToSend(user.getId(), "Ecco la tastiera");
        mts.setReplyMarkup(replyKeyboard);
        sendMessage(mts);
    }

    private ListaDellaSpesa getUserListaSpesa(User user){
        for (ListaDellaSpesa lds : listeSpesa)
            if (lds.getUserID() == user.getId())
                return lds;
        ListaDellaSpesa nuovaLista = new ListaDellaSpesa(user.getId());
        listeSpesa.add(nuovaLista);
        return nuovaLista;
    }
    @Override
    public void audioMessage(Message message) {

    }
    @Override
    public void videoMessage(Message message) {

    }
    @Override
    public void voiceMessage(Message message) {

    }
    @Override
    public void stickerMessage(Message message) {

    }
    @Override
    public void documentMessage(Message message) {

    }
    @Override
    public void photoMessage(Message message) {

    }
    @Override
    public void contactMessage(Message message) {

    }
    @Override
    public void locationMessage(Message message) {

    }
    @Override
    public void venueMessage(Message message) {

    }
    @Override
    public void newChatMemberMessage(Message message) {

    }
    @Override
    public void newChatMembersMessage(Message message) {

    }
    @Override
    public void leftChatMemberMessage(Message message) {

    }
    @Override
    public void newChatTitleMessage(Message message) {

    }
    @Override
    public void newChatPhotoMessage(Message message) {

    }
    @Override
    public void groupChatPhotoDeleteMessage(Message message) {

    }
    @Override
    public void groupChatCreatedMessage(Message message) {

    }
    @Override
    public void inLineQuery(InlineQuery inlineQuery) {

    }
    @Override
    public void chose_inline_result(ChosenInlineResult chosenInlineResult) {

    }
    @Override
    public void callback_query(CallbackQuery callbackQuery) {

    }
    @Override
    public void gameMessage(Message message) {

    }
    @Override
    public void videoNoteMessage(Message message) {

    }
    @Override
    public void pinnedMessage(Message message) {

    }
    @Override
    public void preCheckOutQueryMessage(PreCheckoutQuery preCheckoutQuery) {

    }
    @Override
    public void shippingQueryMessage(ShippingQuery shippingQuery) {

    }
    @Override
    public void invoiceMessage(Message message) {

    }
    @Override
    public void successfulPaymentMessage(Message message) {

    }
    @Override
    public void routine() {

    }

    private class ListaDellaSpesa{
        private long userID;
        private StatoUtente statoUtente;
        private List<ItemSpesa> listaSpesa;

        public ListaDellaSpesa(long userID){
            this.userID=userID;
            this.listaSpesa= new ArrayList<>();
            statoUtente=StatoUtente.NORMALE;
        }

        boolean addItem(String item){
            statoUtente=statoUtente.NORMALE;
            return listaSpesa.add(new ItemSpesa(item));
        }
        boolean addItem(String item, int qta){
            statoUtente=statoUtente.NORMALE;
            return listaSpesa.add(new ItemSpesa(item,qta));
        }

        boolean removeItem(int index){
            statoUtente=StatoUtente.NORMALE;
            if(index >= listaSpesa.size() || index < 0){
                return false;
            }
            listaSpesa.remove(index);
            return true;
        }

        @Override
        public String toString() {
            if(listaSpesa.isEmpty())
                return "Non hai nessun prodotto in lista";
            StringBuilder listString = new StringBuilder();
            for (int i = 0; i < listaSpesa.size(); i++)
            {
                listString.append(i);
                listString.append(" " + listaSpesa.get(i).getItem());
                listString.append(" " + listaSpesa.get(i).getQuantita());
                listString.append('\n');
            }
            listString.deleteCharAt(listString.length() - 1);
            return listString.toString();
        }

        public void setStatoUtente(StatoUtente statoUtente)
        {
            this.statoUtente = statoUtente;
        }

        public StatoUtente getStatoUtente() {
            return statoUtente;
        }

        public long getUserID() {
            return userID;
        }
    }

    private enum ComandoListaSpesa{
        LISTA("lista della spesa"), AGGIUNGI("aggiungi"), RIMUOVI("rimuovi"), ERRORE(""), START("/start");
        private String str;

        private ComandoListaSpesa(String str){
            this.str=str;
        }
        @Override
        public String toString(){
            return str;
        }

        public static ComandoListaSpesa fromString(String txt){
            if(txt!=null){
                for (ComandoListaSpesa c: ComandoListaSpesa.values()){
                    if(txt.equals(c.str)){
                        return c;
                    }
                }
            }
            return ComandoListaSpesa.ERRORE;
        }
    }

    private class ItemSpesa{
        private String item;
        private int quantita;

        public ItemSpesa(String item){
            this.item=item;
            this.quantita=1;
        }
        public ItemSpesa(String item, int quantita){
            this.item=item;
            this.quantita=quantita;
        }
        public String getItem() {
            return item;
        }
        public int getQuantita() {
            return quantita;
        }
    }

    private enum StatoUtente{
        NORMALE,RIMUOVIPRODOTTO,AGGIUNGIPRODOTTO;
    }
}
