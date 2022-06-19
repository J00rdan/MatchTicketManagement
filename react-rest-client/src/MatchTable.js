import React from  'react';
import './MatchApp.css'

class MatchRow extends React.Component{

    handleDelete=(event)=>{
        console.log('delete button pentru '+this.props.user.id);
        this.props.deleteFunc(this.props.user.id);
    }

    // handleGetOne=(event)=>{
    //     console.log('getOne button pentru '+this.props.user.id);
    //     this.props.getOneFunc(this.props.user.id);
    // }


    render() {
        return (
            <tr>
                <td>{this.props.user.id}</td>
                <td>{this.props.user.team1}</td>
                <td>{this.props.user.team2}</td>
                <td>{this.props.user.ticketPrice}</td>
                <td>{this.props.user.seatsAvailable}</td>

                <td><button  onClick={this.handleDelete}>Delete</button></td>

                {/*<td><button  onClick={this.handleGetOne}>Update</button></td>*/}
            </tr>
        );
    }
}

class MatchTable extends React.Component {
    render() {
        let rows = [];
        let functieStergere=this.props.deleteFunc;
        // let functieGetOne=this.props.getOneFunc;
        this.props.users.forEach(function(user) {

            rows.push(<MatchRow user={user}  key={user.id} deleteFunc={functieStergere} />);
        });
        return (<div className="MatchTable">

                <table className="center">
                    <thead>
                    <tr>
                        <th>Match ID</th>
                        <th>Team1</th>
                        <th>Team2</th>
                        <th>TicketPrice</th>
                        <th>SeatsAvailable</th>

                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>

            </div>
        );
    }
}

export default MatchTable