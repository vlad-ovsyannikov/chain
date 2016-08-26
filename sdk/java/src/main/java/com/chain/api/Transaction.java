package com.chain.api;

import com.chain.exception.*;
import com.chain.http.Context;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.*;

/**
 * A single transaction on a Chain Core.
 */
public class Transaction {
    /**
     * Height of the block containing a transaction.
     */
    @SerializedName("block_height")
    public int blockHeight;

    /**
     * Unique identifier, or block hash, of the block containing a transaction.
     */
    @SerializedName("block_id")
    public String blockId;

    /**
     * Unique identifier, or transaction hash, of a transaction.
     */
    public String id;

    /**
     * List of specified inputs for a transaction.
     */
    public List<Input> inputs;

    /**
     * List of specified outputs for a transaction.
     */
    public List<Output> outputs;

    /**
     * Position of a transaction within the block.
     */
    public int position;

    /**
     * User specified, unstructured data embedded within a transaction.
     */
    @SerializedName("reference_data")
    public Map<String, Object> referenceData;

    /**
     * Paged results of a transaction query.
     */
    public static class Items extends PagedItems<Transaction> {
        /**
         * Returns a new page of transactions based on the underlying query.
         * @return a page of transactions
         * @throws APIException This exception is raised if the api returns errors while processing the query.
         * @throws BadURLException This exception wraps java.net.MalformedURLException.
         * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
         * @throws HTTPException This exception is raised when errors occur making http requests.
         * @throws JSONException This exception is raised due to malformed json requests or responses.
         */
        public Items getPage()
        throws ChainException {
            Items items = this.context.request("list-transactions", this.query, Items.class);
            items.setContext(this.context);
            return items;
        }
    }

    /**
     * A builder class for transaction queries.
     */
    public static class QueryBuilder extends BaseQueryBuilder<QueryBuilder> {
        /**
         * Executes a transaction query based on provided parameters.
         * @param ctx context object which makes server requests
         * @return a page of transactions
         * @throws APIException This exception is raised if the api returns errors while processing the query.
         * @throws BadURLException This exception wraps java.net.MalformedURLException.
         * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
         * @throws HTTPException This exception is raised when errors occur making http requests.
         * @throws JSONException This exception is raised due to malformed json requests or responses.
         */
        public Items execute(Context ctx)
        throws ChainException {
            Items items = new Items();
            items.setContext(ctx);
            items.setQuery(this.query);
            return items.getPage();
        }

        /**
         * Sets the earliest transaction timestamp to include in results
         * @param time start time in UTC format
         * @return updated QueryBuilder object
         */
        public QueryBuilder setStartTime(long time) {
            this.query.startTime = time;
            return this;
        }

        /**
         * Sets the latest transaction timestamp to include in results
         * @param time end time in UTC format
         * @return updated QueryBuilder object
         */
        public QueryBuilder setEndTime(long time) {
            this.query.endTime = time;
            return this;
        }
    }

    /**
     * A single input included in a transaction.
     */
    public static class Input {
        /**
         * The type of action being taken on an input.<br>
         * Possible actions are "issue", "spend_account_unspent_output_selector", and "spend_account_unspent_output".
         */
        public String action;

        /**
         * The number of units of the asset being issued or spent.
         */
        public BigInteger amount;

        /**
         * The id of the asset being issued or spent.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The id of the account transferring the asset (possibly null if the input is an issuance or an unspent output is specified).
         */
        @SerializedName("account_id")
        public String accountId;

        /**
         * The tags associated with the account (possibly null).
         */
        @SerializedName("account_tags")
        public Map<String, Object> accountTags;

        /**
         * The tags associated with the asset (possibly null).
         */
        @SerializedName("asset_tags")
        public Map<String, Object> assetTags;

        /**
         * Inputs to the control program used to verify the ability to take the specified action (possibly null).
         */
        @SerializedName("input_witness")
        public byte[][] inputWitness;

        /**
         * A program specifying a predicate for issuing an asset (possibly null if input is not an issuance).
         */
        @SerializedName("issuance_program")
        public byte[] issuanceProgram;

        /**
         * User specified, unstructured data embedded within an input (possibly null).
         */
        @SerializedName("reference_data")
        public Map<String, Object> referenceData;
    }

    /**
     * A single output included in a transaction.
     */
    public static class Output {
        /**
         * The type of action being taken on the output.<br>
         * Possible actions are "control_account", "control_program", and "retire".
         */
        public String action;

        /**
         * The number of units of the asset being controlled.
         */
        public BigInteger amount;

        /**
         * The id of the asset being controlled.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The control program which must be satisfied to transfer this output.
         */
        @SerializedName("control_program")
        public byte[] controlProgram;

        /**
         * The output's position in a transaction's list of outputs
         */
        public int position;

        /**
         * The id of the account controlling this output (possibly null if a control program is specified).
         */
        @SerializedName("account_id")
        public String accountId;

        /**
         * The tags associated with the account controlling this output (possibly null if a control program is specified).
         */
        @SerializedName("account_tags")
        public Map<String, Object> accountTags;

        /**
         * The tags associated with the asset being controlled.
         */
        @SerializedName("asset_tags")
        public Map<String, Object> assetTags;

        /**
         * User specified, unstructured data embedded within an input (possibly null).
         */
        @SerializedName("reference_data")
        public Map<String, Object> referenceData;
    }

    /**
     * A built transaction that has not been submitted for block inclusion (returned from {@link Transaction#build(Context, List)}).
     */
    public static class Template {
        /**
         * A hex-encoded representation of a transaction template.
         */
        @SerializedName("unsigned_hex")
        public String unsignedHex;

        /**
         * The list of inputs included in a transaction.
         */
        public List<Input> inputs;

        /**
         * For core use only.
         */
        private Boolean local;

        /**
         * A single input included in a transaction template.
         */
        public static class Input {
            /**
             * The id of the asset being issued or spent.
             */
            @SerializedName("asset_id")
            public String assetID;

            /**
             * The number of units of the asset being issued or spent.
             */
            public BigInteger amount;

            /**
             * The input's position in a transaction's list of inputs.
             */
            public int position;

            /**
             * A list of components used to coordinate the signing an input.
             */
            @SerializedName("signature_components")
            public SignatureComponent[] signatureComponents;
        }

        /**
         * A single signature component, holding information that will become the input witness.
         */
        public static class SignatureComponent {
            /**
             * The type of signature component.<br>
             * Possible types are "script", "data", and "signature".
             */
            public String type;

            /**
             * Data to be included in the input witness (null unless type is "data").
             */
            public String data;

            /**
             * The number of signatures required for an input (null unless type is "signature").
             */
            public int quorum;

            /**
             * The data which needs to be signed (null unless type is "signature").
             */
            @SerializedName("signature_data")
            public String signatureData;

            /**
             * The list of signatures (null unless type is "signature").
             */
            public Signature[] signatures;
        }

        /**
         * A class representing a signature on an input.
         */
        public static class Signature {
            /**
             * The extended public key associated with the private key used to sign.
             */
            public String xpub;

            /**
             * The derivation path of the extended public key.
             */
            @SerializedName("derivation_path")
            public ArrayList<Integer> derivationPath;

            /**
             * The hex-encoded signature.
             */
            public String signature;
        }
    }

    /**
     * A single response from a call to {@link Transaction#submit(Context, List)}
     */
    public static class SubmitResponse {
        /**
         * The transaction id.
         */
        public String id;

        /**
         * The Chain error code.
         */
        public String code;

        /**
         * The Chain error message.
         */
        public String message;

        /**
         * Additional details about the error.
         */
        public String detail;
    }

    /**
     * Builds a transaction template.
     * @param ctx context object which makes server requests
     * @param builders list of transaction builders
     * @return a list of transaction templates
     * @throws APIException This exception is raised if the api returns errors while building transaction templates.
     * @throws BadURLException This exception wraps java.net.MalformedURLException.
     * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
     * @throws HTTPException This exception is raised when errors occur making http requests.
     * @throws JSONException This exception is raised due to malformed json requests or responses.
     */
    public static List<Template> build(Context ctx, List<Transaction.Builder> builders)
    throws ChainException {
        Type type = new TypeToken<ArrayList<Template>>() {
        }.getType();
        return ctx.request("build-transaction-template", builders, type);
    }

    /**
     * Submits signed transaction templates for inclusion into a block.
     * @param ctx context object which makes server requests
     * @param templates list of transaction templates
     * @return a list of submit responses (individual objects can hold transaction ids or error info)
     * @throws APIException This exception is raised if the api returns errors while submitting transactions.
     * @throws BadURLException This exception wraps java.net.MalformedURLException.
     * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
     * @throws HTTPException This exception is raised when errors occur making http requests.
     * @throws JSONException This exception is raised due to malformed json requests or responses.
     */
    public static List<SubmitResponse> submit(Context ctx, List<Template> templates)
    throws ChainException {
        Type type = new TypeToken<ArrayList<SubmitResponse>>() {
        }.getType();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("transactions", templates);

        return ctx.request("submit-transaction-template", requestBody, type);
    }

    /**
     * An action that can be taken within a transaction.
     */
    public static class Action {
        /**
         * The type of action.<br>
         * Possible types are "issue", "spend_account_unspent_output_selector", "spend_account_unspent_output", "control_account", "control_program", and "retire".
         */
        public String type;

        /**
         * List of parameters used to build the action.
         */
        public HashMap<String, Object> params;

        /**
         * User specified, unstructured data embedded within the action (possibly null).
         */
        @SerializedName("reference_data")
        public Map<String, Object> referenceData;

        /**
         * A unique identifier used for request idempotence.
         */
        @SerializedName("client_token")
        private String clientToken;

        /**
         * Default constructor initializes list and sets the client token.
         */
        public Action() {
            this.params = new HashMap();
            this.clientToken = UUID.randomUUID().toString();
        }

        /**
         * Sets the action type.
         * @param type the type of action
         * @return updated action object
         */
        public Action setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Sets a k,v parameter pair.
         * @param key the key on the parameter object
         * @param value the corresponding value
         * @return updated action object
         */
        public Action setParameter(String key, Object value) {
            this.params.put(key, value);
            return this;
        }

        /**
         * Sets the reference data for the action.
         * @param referenceData a map (represented as a json object) of reference information
         * @return updated action object
         */
        public Action setReferenceData(Map<String, Object> referenceData) {
            this.referenceData = referenceData;
            return this;
        }
    }

    /**
     * A builder class for transaction templates.
     */
    public static class Builder {
        /**
         * List of actions in a transaction.
         */
        private List<Action> actions;

        /**
         * User specified, unstructured data embedded within the action.
         */
        @SerializedName("reference_data")
        private Map<String, Object> referenceData;

        /**
         * Builds a single transaction template.
         * @param ctx context object which makes requests to the server
         * @return a transaction template
         * @throws APIException This exception is raised if the api returns errors while building the transaction.
         * @throws BadURLException This exception wraps java.net.MalformedURLException.
         * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
         * @throws HTTPException This exception is raised when errors occur making http requests.
         * @throws JSONException This exception is raised due to malformed json requests or responses.
         */
        public Template build(Context ctx)
        throws ChainException {
            List<Template> tmpls = Transaction.build(ctx, Arrays.asList(this));
            return tmpls.get(0);
        }

        /**
         * Default constructor initializes actions list.
         */
        public Builder() {
            this.actions = new ArrayList<>();
        }

        /**
         * Adds an action to a transaction builder.
         * @param action action to add
         * @return updated builder object
         */
        public Builder addAction(Action action) {
            this.actions.add(action);
            return this;
        }

        /**
         * Adds an action to a transaction builder.
         * @param action action to add
         * @param referenceData reference info to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder addAction(Action action, Map<String, Object> referenceData) {
            if (referenceData != null) {
                action.setReferenceData(referenceData);
            }

            this.actions.add(action);
            return this;
        }

        /**
         * Sets a transaction's reference data.
         * @param referenceData info to embed into a transaction.
         * @return
         */
        public Builder setReferenceData(Map<String, Object> referenceData) {
            this.referenceData = referenceData;
            return this;
        }

        /**
         * Adds an issuance action to a transaction, using an id to specify the asset.
         * @param assetId id of the asset being issued
         * @param amount number of units of the asset to issue
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder issueById(String assetId, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("issue")
                    .setParameter("asset_id", assetId)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds an issuance action to a transaction, using an alias to specify the asset.
         * @param assetAlias alias of the asset being issued
         * @param amount number of units of the asset to issue
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         * @return
         */
        public Builder issueByAlias(String assetAlias, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("issue")
                    .setParameter("asset_alias", assetAlias)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a control (by account) action to a transaction, using an id to specify the asset and account.
         * @param accountId id of the account controlling the asset
         * @param assetId id of the asset being controlled
         * @param amount number of units of the asset being controlled
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder controlWithAccountByID(String accountId, String assetId, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("control_account")
                    .setParameter("account_id", accountId)
                    .setParameter("asset_id", assetId)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a control (by account) action to a transaction, using an alias to specify the asset and account.
         * @param accountAlias alias of the account controlling the asset
         * @param assetAlias alias of the asset being controlled
         * @param amount number of units of the asset being controlled
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder controlWithAccountByAlias(String accountAlias, String assetAlias, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("control_account")
                    .setParameter("account_alias", accountAlias)
                    .setParameter("asset_alias", assetAlias)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a control (by control program) action to a transaction, using an id to specify the asset.
         * @param program control program which will control the asset
         * @param assetId id of the asset being controlled
         * @param amount number of units of the asset being controlled
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder controlWithProgramById(ControlProgram program, String assetId, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("control_program")
                    .setParameter("control_program", program.program)
                    .setParameter("asset_id", assetId)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a control (by control program) action to a transaction, using an alias to specify the asset.
         * @param program control program which will control the asset
         * @param assetAlias alias of the asset being controlled
         * @param amount number of units of the asset being controlled
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder controlWithProgramByAlias(ControlProgram program, String assetAlias, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("control_program")
                    .setParameter("control_program", program.program)
                    .setParameter("asset_alias", assetAlias)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a spend (by account) action to a transaction, using an id to specify the account and asset.
         * @param accountId id of the account spending the asset
         * @param assetId id of the asset being spent
         * @param amount number of units of the asset being spent
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder spendFromAccountById(String accountId, String assetId, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("spend_account_unspent_output_selector")
                    .setParameter("account_id", accountId)
                    .setParameter("asset_id", assetId)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a spend (by account) action to a transaction, using an alias to specify the account and asset.
         * @param accountAlias alias of the account spending the asset
         * @param assetAlias alias of the asset being spent
         * @param amount number of units of the asset being spent
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder spendFromAccountByAlias(String accountAlias, String assetAlias, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("spend_account_unspent_output_selector")
                    .setParameter("account_alias", accountAlias)
                    .setParameter("asset_alias", assetAlias)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a spend (by unspent output) action to a transaction.
         * @param unspentOutput unspent output to spend
         * @param referenceData reference data to embed into action (possibly null)
         * @return
         */
        public Builder spendUnspentOutput(UnspentOutput unspentOutput, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("spend_account_unspent_output")
                    .setParameter("transaction_id", unspentOutput.transactionId)
                    .setParameter("position", unspentOutput.position);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a list of spend (by unspent output) actions to a transaction.
         * @param uos list of unspent outputs to spend
         * @param referenceData reference data to embed into each action (possibly null)
         * @return updated builder object
         */
        public Builder spendUnspentOutputs(List<UnspentOutput> uos, Map<String, Object> referenceData) {
            for (UnspentOutput uo : uos) {
                this.spendUnspentOutput(uo, referenceData);
            }

            return this;
        }

        /**
         * Adds a retire action to a transaction, using an id to specify the asset.
         * @param assetId id of the asset to retire
         * @param amount number of units of the asset to retire
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder retireById(String assetId, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("control_program")
                    .setParameter("control_program", ControlProgram.retireProgram())
                    .setParameter("asset_id", assetId)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }

        /**
         * Adds a retire action to a transaction, using an alias to specify the asset.
         * @param assetAlias id of the asset to retire
         * @param amount number of units of the asset to retire
         * @param referenceData reference data to embed into the action (possibly null)
         * @return updated builder object
         */
        public Builder retireByAlias(String assetAlias, BigInteger amount, Map<String, Object> referenceData) {
            Action action = new Action()
                    .setType("control_program")
                    .setParameter("control_program", ControlProgram.retireProgram())
                    .setParameter("asset_alias", assetAlias)
                    .setParameter("amount", amount);

            return this.addAction(action, referenceData);
        }
    }
}