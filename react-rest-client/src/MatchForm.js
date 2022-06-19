import React from  'react';
class MatchForm extends React.Component{

    constructor(props) {
        super(props);
        this.state = {id: '', team1: '', team2:'', ticketPrice:'', seatsAvailable: ''};

        //  this.handleChange = this.handleChange.bind(this);
        // this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleIdChange=(event) =>{
        this.setState({id: event.target.value});
    }

    handleTeam1Change=(event) =>{
        this.setState({team1: event.target.value});
    }

    handleTeam2Change=(event) =>{
        this.setState({team2: event.target.value});
    }

    handleSeatsAvailableChange=(event) =>{
        this.setState({seatsAvailable: event.target.value});
    }

    handleTicketPriceChange=(event) =>{
        this.setState({ticketPrice: event.target.value});
    }

    handleSubmitAdd =(event) =>{

        let match={team1:this.state.team1,
            team2:this.state.team2,
            ticketPrice:this.state.ticketPrice,
            seatsAvailable:this.state.seatsAvailable
        }
        console.log('A match was submitted: ');
        console.log(match);
        this.props.addFunc(match);
        event.preventDefault();
    }

    handleSubmitUpdate =(event) =>{

        let match={id:this.state.id,
            team1:this.state.team1,
            team2:this.state.team2,
            ticketPrice:this.state.ticketPrice,
            seatsAvailable:this.state.seatsAvailable
        }
        console.log('A match was updated: ');
        console.log(match);
        this.props.updateFunc(match);
        event.preventDefault();
    }

    render() {
        return (
            <form>
                <label>
                    Match ID:
                    <input id="MatchId" type="text" value={this.state.id} onChange={this.handleIdChange} />
                </label><br/>
                <label>
                    Team1:
                    <input id="Team1Input" type="text" value={this.state.team1} onChange={this.handleTeam1Change} />
                </label><br/>
                <label>
                    Team2:
                    <input id="Team2Input" type="text" value={this.state.team2} onChange={this.handleTeam2Change} />
                </label><br/>
                <label>
                    Ticket Price:
                    <input type="text" value={this.state.ticketPrice} onChange={this.handleTicketPriceChange} />
                </label><br/>
                <label>
                    Seats Available:
                    <input id="MatchId" type="text" value={this.state.seatsAvailable} onChange={this.handleSeatsAvailableChange} />
                </label><br/>

                <input type="submit" value="Add match" onClick={this.handleSubmitAdd}/>
                <input type="submit" value="Update match" onClick={this.handleSubmitUpdate}/>

            </form>
        );
    }
}
export default MatchForm;